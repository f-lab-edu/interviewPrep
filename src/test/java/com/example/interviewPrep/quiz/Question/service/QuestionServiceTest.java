package com.example.interviewPrep.quiz.Question.service;

import com.example.interviewPrep.quiz.member.controller.MemberController;
import com.example.interviewPrep.quiz.member.social.service.GoogleOauth;
import com.example.interviewPrep.quiz.member.social.service.KakaoOauth;
import com.example.interviewPrep.quiz.member.social.service.NaverOauth;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.question.dto.QuestionRequest;
import com.example.interviewPrep.quiz.question.repository.QuestionRepository;
import com.example.interviewPrep.quiz.question.service.QuestionService;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class QuestionServiceTest {

    @Autowired
    private QuestionService questionService;

    @MockBean
    private QuestionRepository questionRepository;

    @MockBean
    MemberController memberController;

    @MockBean
    GoogleOauth googleOauth;

    @MockBean
    KakaoOauth kakaoOauth;

    @MockBean
    NaverOauth naverOauth;

    Question question;

    @BeforeEach
    void setUp(){

        question = Question.builder()
                            .id(1L)
                            .title("problem1")
                            .type("java")
                            .build();

        List<Question> questionList = List.of(question);

        given(questionRepository.save(question)).willReturn(question);
        given(questionRepository.findAll()).willReturn(questionList);
        given(questionRepository.findById(1L)).willReturn(Optional.of(question));
        given(questionRepository.findById(1000L)).willThrow(CommonException.class);
    }

    @Test
    @DisplayName("Question 생성")
    void createQuestion() {

        QuestionRequest questionRequest = QuestionRequest.builder()
                                          .title("problem1")
                                          .type("java")
                                          .build();

        Question createdQuestion = questionService.createQuestion(questionRequest);

        assertThat(createdQuestion.getTitle()).isEqualTo("problem1");
        assertThat(createdQuestion.getType()).isEqualTo("java");
    }

    @Test
    @DisplayName("유효한 ID로 Question 업데이트")
    void updateQuestionWithExistedId(){

       QuestionRequest questionRequest = QuestionRequest.builder()
                                        .title("problem2")
                                        .type("c++")
                                        .build();

       Question updatedQuestion = questionService.updateQuestion(1L, questionRequest);

       assertThat(updatedQuestion.getId()).isEqualTo(1L);
       assertThat(updatedQuestion.getTitle()).isEqualTo("problem2");
       assertThat(updatedQuestion.getType()).isEqualTo("c++");
    }

    @Test
    @DisplayName("유효하지 않은 ID로 Question 업데이트")
    void updateQuestionWithNotExistedId(){

        QuestionRequest questionRequest = QuestionRequest.builder()
                                .title("problem1000")
                                .type("java")
                                .build();

        assertThatThrownBy(() -> questionService.updateQuestion(1000L, questionRequest))
                .isInstanceOf(CommonException.class);

    }


    @Test
    @DisplayName("유효한 ID로 Question 삭제")
    void deleteQuestionWithExistedId(){

        questionService.deleteQuestion(1L);

        verify(questionRepository).delete(any(Question.class));
    }

    @Test
    @DisplayName("유효하지 않은 ID로 Question 삭제")
    void deleteQuestionWithNotExistedId(){

        assertThatThrownBy(() -> questionService.deleteQuestion(1000L))
                            .isInstanceOf(CommonException.class);
    }


}
