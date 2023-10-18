package com.example.interviewPrep.quiz.Question.controller;

import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.company.repository.CompanyRepository;
import com.example.interviewPrep.quiz.config.CustomAuthenticationEntryPoint;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.exception.advice.ErrorCode;
import com.example.interviewPrep.quiz.filter.JwtAuthenticationFilter;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.question.controller.QuestionController;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.question.dto.QuestionRequest;
import com.example.interviewPrep.quiz.question.repository.QuestionRepository;
import com.example.interviewPrep.quiz.questionCompany.repository.QuestionCompanyRepository;
import com.example.interviewPrep.quiz.security.WithMockCustomOAuth2Account;
import com.example.interviewPrep.quiz.member.service.CustomOAuth2UserService;
import com.example.interviewPrep.quiz.question.service.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_QUESTION;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockCustomOAuth2Account()
@WebMvcTest(QuestionController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
public class QuestionUpdateWebControllerTest {

    @MockBean
    QuestionService questionService;

    @MockBean
    JwtService jwtService;

    @MockBean
    QuestionRepository questionRepository;

    @MockBean
    CompanyRepository companyRepository;


    @MockBean
    QuestionCompanyRepository questionCompanyRepository;

    @MockBean
    AnswerRepository answerRepository;

    @MockBean
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @MockBean
    CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    MockMvc mockMvc;

    QuestionRequest validUpdateQuestionRequest;
    QuestionRequest InvalidUpdateQuestionRequest;

    String validUpdateJsonRequest;
    String InvalidUpdateJsonRequest;

    Question question;

    @BeforeEach
    void setUp() throws Exception{

       // questionService = new QuestionService(jwtService, questionRepository, companyRepository, questionCompanyRepository, answerRepository);

       question = Question.builder()
                .id(1L)
                .title("problem1")
                .type("java")
                .difficulty("easy")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        validUpdateQuestionRequest = QuestionRequest.builder()
                .title("problem1")
                .type("java")
                .difficulty("easy")
                .build();

        InvalidUpdateQuestionRequest = QuestionRequest.builder()
                .title("problem2")
                .type("java")
                .difficulty("easy")
                .build();


        validUpdateJsonRequest = objectMapper.writeValueAsString(validUpdateQuestionRequest);
        InvalidUpdateJsonRequest = objectMapper.writeValueAsString(InvalidUpdateQuestionRequest);
    }


    @Test
    @DisplayName("유효한 Question 업데이트 요청")
    void updateWithExistedQuestion() throws Exception{

        given(questionService.updateQuestion(1L, validUpdateQuestionRequest)).willReturn(question);

        mockMvc.perform(put("/api/v1/questions/"+1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUpdateJsonRequest))
                        .andDo(print())
                        .andExpect(status().isOk());

        verify(questionService).updateQuestion(eq(1L), any(QuestionRequest.class));
    }

    // 테스트 수정 필요
    @Test
    @DisplayName("유효하지 않은 Question 업데이트 요청")
    void updateWithNotExistedQuestion() throws Exception{

        given(questionRepository.findById(1000L)).willThrow(new CommonException(NOT_FOUND_QUESTION));

        mockMvc.perform(put("/api/v1/questions/"+1000L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(InvalidUpdateJsonRequest))
                        .andDo(print())
                        .andExpect(status().isOk());

         verify(questionService).updateQuestion(eq(1000L), any(QuestionRequest.class));
    }
}
