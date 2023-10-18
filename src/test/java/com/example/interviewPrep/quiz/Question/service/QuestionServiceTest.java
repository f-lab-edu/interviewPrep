package com.example.interviewPrep.quiz.Question.service;

import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.company.repository.CompanyRepository;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.member.controller.MemberController;
import com.example.interviewPrep.quiz.member.social.service.GoogleOauth;
import com.example.interviewPrep.quiz.member.social.service.KakaoOauth;
import com.example.interviewPrep.quiz.member.social.service.NaverOauth;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.question.dto.QuestionRequest;
import com.example.interviewPrep.quiz.question.repository.QuestionRepository;
import com.example.interviewPrep.quiz.question.service.QuestionService;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.questionCompany.repository.QuestionCompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    private QuestionService questionService;

    @Mock
    private JwtService jwtService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private QuestionCompanyRepository questionCompanyRepository;

    @Mock
    private AnswerRepository answerRepository;

    Question question;

    @BeforeEach
    void setUp(){

        questionService = new QuestionService(jwtService, questionRepository, companyRepository, questionCompanyRepository, answerRepository);

        question = Question.builder()
                            .id(1L)
                            .title("problem1")
                            .type("java")
                            .difficulty("easy")
                            .build();
    }

    @Test
    @DisplayName("Question 생성")
    void createQuestion() {

        QuestionRequest questionRequest = QuestionRequest.builder()
                                          .title("problem1")
                                          .type("java")
                                          .difficulty("easy")
                                          .build();

        Question createdQuestion = questionService.createQuestion(questionRequest);

        assertThat(createdQuestion.getTitle()).isEqualTo("problem1");
        assertThat(createdQuestion.getType()).isEqualTo("java");
        assertThat(createdQuestion.getDifficulty()).isEqualTo("easy");
    }

    @Test
    @DisplayName("유효한 ID로 Question 업데이트")
    void updateQuestionWithExistedId(){

       given(questionRepository.findById(1L)).willReturn(Optional.of(question));

       QuestionRequest questionRequest = QuestionRequest.builder()
                                        .title("problem2")
                                        .type("c++")
                                        .difficulty("easy")
                                        .build();

       Question updatedQuestion = questionService.updateQuestion(1L, questionRequest);

       assertThat(updatedQuestion.getId()).isEqualTo(1L);
       assertThat(updatedQuestion.getTitle()).isEqualTo("problem2");
       assertThat(updatedQuestion.getType()).isEqualTo("c++");
       assertThat(updatedQuestion.getDifficulty()).isEqualTo("easy");
    }

    @Test
    @DisplayName("유효하지 않은 ID로 Question 업데이트")
    void updateQuestionWithNotExistedId(){

        given(questionRepository.findById(1000L)).willThrow(CommonException.class);

        QuestionRequest questionRequest = QuestionRequest.builder()
                                .title("problem1000")
                                .type("java")
                                .difficulty("easy")
                                .build();

        assertThatThrownBy(() -> questionService.updateQuestion(1000L, questionRequest))
                .isInstanceOf(CommonException.class);

    }


    @Test
    @DisplayName("유효한 ID로 Question 삭제")
    void deleteQuestionWithExistedId(){

        given(questionRepository.findById(1L)).willReturn(Optional.of(question));

        questionService.deleteQuestion(1L);

        verify(questionRepository).delete(any(Question.class));
    }

    @Test
    @DisplayName("유효하지 않은 ID로 Question 삭제")
    void deleteQuestionWithNotExistedId(){

        given(questionRepository.findById(1000L)).willThrow(CommonException.class);

        assertThatThrownBy(() -> questionService.deleteQuestion(1000L))
                            .isInstanceOf(CommonException.class);
    }


}
