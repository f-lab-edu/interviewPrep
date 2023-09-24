package com.example.interviewPrep.quiz.answer.service;

import com.example.interviewPrep.quiz.answer.dto.request.AnswerRequest;
import com.example.interviewPrep.quiz.answer.dto.response.AnswerResponse;
import com.example.interviewPrep.quiz.answer.dto.response.SolutionResponse;
import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.heart.repository.HeartRepository;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.question.repository.QuestionRepository;
import com.example.interviewPrep.quiz.utils.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.interviewPrep.quiz.answer.domain.Answer.createAnswerEntity;
import static com.example.interviewPrep.quiz.answer.dto.response.AnswerResponse.createAnswerResponse;
import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;
import static com.example.interviewPrep.quiz.utils.DateFormat.customLocalDateTime;


@Service
public class AnswerService {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final HeartRepository heartRepository;

    public AnswerService(JwtService jwtService, MemberRepository memberRepository, AnswerRepository answerRepository, QuestionRepository questionRepository, HeartRepository heartRepository){
        this.jwtService = jwtService;
        this.memberRepository = memberRepository;
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.heartRepository = heartRepository;
    }

    public AnswerResponse createAnswer(AnswerRequest answerRequest){

        Long memberId = jwtService.getMemberId();

        Member answerMember = memberRepository.findById(memberId).orElse(null);
        Question answerQuestion = questionRepository.findById(answerRequest.getQuestionId()).orElse(null);

        Answer answer = createAnswerEntity(answerMember, answerQuestion, answerRequest.getContent());

        answerRepository.save(answer);

        return createAnswerResponse(answer);
    }

    public AnswerResponse readAnswer(Long id){

        Optional<Answer> answer = answerRepository.findById(id);

        if(answer.isEmpty()){
            throw new CommonException(NOT_FOUND_ANSWER);
        }

        Answer answerResponse = answer.get();

        return createAnswerResponse(answerResponse);
    }


    public Answer deleteAnswer(Long id){

        Optional<Answer> answer = answerRepository.findById(id);

        if(answer.isEmpty()){
            throw new CommonException(NOT_FOUND_ANSWER);
        }

        Answer deleteAnswer = answer.get();
        answerRepository.delete(deleteAnswer);

        return deleteAnswer;
    }


    public Page<SolutionResponse> getSolution(Long id, String type, Pageable pageable){

        Long memberId = jwtService.getMemberId();
        Page<Answer> answers;

        Type inputType = Type.valueOf(type.toUpperCase());

        if(!(inputType.equals(Type.MY) || inputType.equals(Type.OTHERS))){
            throw new CommonException(NOT_FOUND_TYPE);
        }

        if(inputType.equals(Type.MY)) {
            answers = answerRepository.findMySolution(id, memberId, pageable);
            if(answers.getContent().isEmpty()){
                throw new CommonException(NOT_FOUND_ANSWER);
            }

            return makeSolutionResponse(answers, new ArrayList<>());
        }


        answers = answerRepository.findSolution(id, memberId, pageable);

        if(answers.getContent().isEmpty()) {
            throw new CommonException(NOT_FOUND_ANSWER);
        }

        List<Long> answerIds = answers.getContent().stream().map(Answer::getId).collect(Collectors.toList());
        List<Long> myHeartIds = heartRepository.findMyHeart(answerIds, memberId);
        return makeSolutionResponse(answers, myHeartIds);

    }


    public Page<SolutionResponse> makeSolutionResponse(Page<Answer> answers, List<Long> myHeartIds){
           return answers.map(answer-> SolutionResponse.builder()
                            .answerId(answer.getId())
                            .answer(answer.getContent())
                            .heartCnt(answer.getHeartCnt())
                            .commentCnt(answer.getCommentCnt())
                            .name(answer.getMember().getName())
                            .heart(myHeartIds.contains(answer.getId()))
                            .createdDate(customLocalDateTime(answer.getCreatedDate()))
                            .modifiedDate(customLocalDateTime(answer.getModifiedDate()))
                            .modify(!answer.getCreatedDate().equals(answer.getModifiedDate()))
                            .build());
    }


    public void checkMySolution(Long id){
        Long memberId = jwtService.getMemberId();

        List<Answer> answers = answerRepository.findAllByQuestionIdAndMemberId(id, memberId);

        if(answers.isEmpty()) {
            throw new CommonException(NOT_FOUND_ANSWER);
        }
    }

}