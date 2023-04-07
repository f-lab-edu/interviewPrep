package com.example.interviewPrep.quiz.answer.service;


import com.example.interviewPrep.quiz.answer.dto.SolutionDTO;
import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.answer.dto.AnswerDTO;
import com.example.interviewPrep.quiz.dto.CreateDto;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.heart.repository.HeartRepository;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.question.repository.QuestionRepository;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;
import static com.example.interviewPrep.quiz.utils.DateFormat.customLocalDateTime;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final HeartRepository heartRepository;

    public CreateDto createAnswer(AnswerDTO answerDTO){

        Long memberId = JwtUtil.getMemberId();

        Member member = memberRepository.findById(memberId).get();
        Question question = questionRepository.findById(answerDTO.getQuestionId()).get();

        Answer answer =  Answer.builder()
                .member(member)
                .question(question)
                .content(answerDTO.getContent())
                .build();

        answerRepository.save(answer);

        return CreateDto.builder()
                .id(answer.getId())
                .createDate(customLocalDateTime(answer.getCreatedDate()))
                .name(answer.getMember().getName())
                .build();
    }

    public AnswerDTO readAnswer(Long id){

        Answer answer = answerRepository.findById(id).get();

        return AnswerDTO.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .questionId(answer.getQuestion().getId())
                .build();
    }


    public Answer deleteAnswer(Long id){

        Answer answer = answerRepository.findById(id).get();

        answerRepository.delete(answer);
        return answer;

    }


    public Page<SolutionDTO> getSolution(Long id, String type, Pageable pageable){

        Long memberId = JwtUtil.getMemberId();
        Page<Answer> answers;

        if(type.equals("my")) {
            answers = answerRepository.findMySolution(id, memberId, pageable);
            if(answers.getContent().isEmpty()) throw new CommonException(NOT_FOUND_ANSWER);
            return makeSolutionDto(answers, new ArrayList<>());
        }
        else if(type.equals("others")){
            answers = answerRepository.findSolution(id, memberId, pageable);
            if(answers.getContent().isEmpty()) throw new CommonException(NOT_FOUND_ANSWER);

            List<Long> aList = answers.getContent().stream().map(Answer::getId).collect(Collectors.toList());
            List<Long> myHeart = heartRepository.findMyHeart(aList, memberId);
            return makeSolutionDto(answers, myHeart);
        }
        else{
            throw new CommonException(NOT_FOUND_TYPE);
        }
    }


    public Page<SolutionDTO> makeSolutionDto(Page<Answer> answers, List<Long> myHeart){
           return answers.map(a-> SolutionDTO.builder()
                    .answerId(a.getId())
                    .answer(a.getContent())
                    .heartCnt(a.getHeartCnt())
                   .commentCnt(a.getCommentCnt())
                    .name(a.getMember().getName())
                    .heart(myHeart.contains(a.getId()))
                    .createdDate(customLocalDateTime(a.getCreatedDate()))
                    .modifiedDate(customLocalDateTime(a.getModifiedDate()))
                    .modify(!a.getCreatedDate().equals(a.getModifiedDate()))
                    .build());
    }


    public void checkMySolution(Long id){
        Long memberId = JwtUtil.getMemberId();
        if(memberId==0L) throw new CommonException(NOT_FOUND_ID);

        List<Answer> answers = answerRepository.findAllByQuestionIdAndMemberId(id, memberId);
        if(answers.isEmpty()) throw new CommonException(NOT_FOUND_ANSWER);
    }

}