package com.example.interviewPrep.quiz.Answer.service;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.heart.repository.HeartRepository;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.answer.dto.AnswerDTO;
import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.question.repository.QuestionRepository;
import com.example.interviewPrep.quiz.answer.service.AnswerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AnswerServiceTest {

    private AnswerService answerService;

    private final AnswerRepository answerRepository = mock(AnswerRepository .class);
    private final QuestionRepository questionRepository =  mock(QuestionRepository.class);
    private final MemberRepository memberRepository =  mock(MemberRepository.class);
    private final HeartRepository heartRepository = mock(HeartRepository.class);


    Answer answer1;
    Answer answer2;
    List<Answer> answers;
    AnswerDTO answerDTO1;
    AnswerDTO answerDTO2;
    List<AnswerDTO> answerDTOs;

    Question question;


    @BeforeEach
    public void setUp(){

        answerService = new AnswerService(memberRepository, answerRepository,
                questionRepository, heartRepository);

        answerDTOs = new ArrayList<>();

        answerDTO1 = AnswerDTO.builder()
                .content("새 답안입니다.")
                .questionId(1L)
                .build();

        answerDTO2 = AnswerDTO.builder()
                .content("새 답안입니다2.")
                .build();

        answerDTOs.add(answerDTO1);
        answerDTOs.add(answerDTO2);

        question = Question.builder()
                .id(1L)
                .build();

        when(questionRepository.findById(1L)).thenReturn(Optional.ofNullable(question));
    }


    @Test
    public void createAnswers(){

        answers = new ArrayList<>();

        answer1 = Answer.builder()
                .content(answerDTO1.getContent())
                .build();

        answer2 = Answer.builder()
                .content(answerDTO2.getContent())
                .build();

        answers.add(answer1);
        answers.add(answer2);

        int order = 0;
        for(Answer answer: answers){
            assertThat(answer.getContent()).isEqualTo(answerDTOs.get(order++).getContent());
        }
    }


    @Test
    @DisplayName("single answer create")
    public void createAnswer(){

        //given
        answer1 = Answer.builder()
                .content(answerDTO1.getContent())
                .build();

        //when
        Answer myAns = answerService.createAnswer(answerDTO1);

        //then
        assertThat(myAns.getContent()).isEqualTo(answer1.getContent());

    }



}
