package com.example.interviewPrep.quiz.question.service;

import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.question.dto.FilterDTO;
import com.example.interviewPrep.quiz.question.dto.QuestionDTO;
import com.example.interviewPrep.quiz.question.repository.QuestionRepository;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_QUESTION;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_QUESTION;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public List<Question> getQuestions() {return questionRepository.findAll();}


    //@Cacheable(value = "question", key="#id")
    public QuestionDTO getQuestion(Long id) {
        Question question = findQuestion(id);

        return QuestionDTO.builder()
                .id(question.getId())
                .title(question.getTitle())
                .type(question.getType())
                .build();
    }


    public Question createQuestion(QuestionDTO questionDTO){
        Question question = Question.builder()
                .id(questionDTO.getId())
                .title(questionDTO.getTitle())
                .type(questionDTO.getType())
                .build();
        questionRepository.save(question);
        return question;
    }

    public Question updateQuestion(Long id, QuestionDTO questionDTO){
        Question question = findQuestion(id);
        question.change(questionDTO.getTitle(), questionDTO.getType());
        return question;
    }

    public Question deleteQuestion(Long id){
        Question question = findQuestion(id);
        questionRepository.delete(question);
        return question;
    }

    public List<Question> findQuestionsByType(String type){
        return questionRepository.findByType(type);
    }

    //@Cacheable(value = "questionDTO", key="#pageable.pageSize.toString().concat('-').concat(#pageable.pageNumber)")
    public Page<QuestionDTO> findByType(String type, Pageable pageable){
        Long memberId = JwtUtil.getMemberId();
        Page<Question> questions;

        if(type==null) questions = questionRepository.findAllBy(pageable);
        else questions = questionRepository.findByType(type, pageable); //문제 타입과 페이지 조건 값을 보내어 question 조회, 반환값 page

        if(questions.getContent().isEmpty()) throw new CommonException(NOT_FOUND_QUESTION);

        if(memberId==0L){
            return makeQuestionDto(questions, new ArrayList<>());
        }
        else{
            List<Long> qList = questions.getContent().stream().map(Question::getId).collect(Collectors.toList());
            List<Long> myAnswer = answerRepository.findMyAnswer(qList, memberId);
            return makeQuestionDto(questions, myAnswer);
        }

    }

    public Page<QuestionDTO> makeQuestionDto(Page<Question> questions, List<Long> myAnswer){
        return questions.map(q -> QuestionDTO.builder()
                        .id(q.getId())
                        .type(q.getType())
                        .title(q.getTitle())
                        .status(myAnswer.contains(q.getId()))
                        .build());
    }


    public Question findQuestion(Long id){
        return questionRepository.findById(id).orElseThrow(() -> new CommonException(NOT_FOUND_QUESTION));
    }


    public List<FilterDTO> findFilterLanguage(){
        List<FilterDTO> filterDTOS = new ArrayList<>();

        List<String> languages = questionRepository.findAllByLanguage();

        for(String language: languages){
            FilterDTO filterDTO = FilterDTO.builder()
                    .language(language)
                    .build();
            filterDTOS.add(filterDTO);
        }

        return filterDTOS;
    }


}