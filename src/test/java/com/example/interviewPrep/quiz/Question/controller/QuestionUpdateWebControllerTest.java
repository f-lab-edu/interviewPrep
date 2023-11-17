package com.example.interviewPrep.quiz.Question.controller;

import com.example.interviewPrep.quiz.config.CustomAuthenticationEntryPoint;
import com.example.interviewPrep.quiz.filter.JwtAuthenticationFilter;
import com.example.interviewPrep.quiz.member.service.CustomOAuth2UserService;
import com.example.interviewPrep.quiz.question.controller.QuestionController;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.question.dto.QuestionRequest;
import com.example.interviewPrep.quiz.question.exception.QuestionNotFoundException;
import com.example.interviewPrep.quiz.security.WithMockCustomOAuth2Account;
import com.example.interviewPrep.quiz.question.service.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockCustomOAuth2Account()
@WebMvcTest(QuestionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class QuestionUpdateWebControllerTest {

    @MockBean
    QuestionService questionService;

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


    @BeforeEach
    void setUp() throws Exception{

       Question question = Question.builder()
                .id(1L)
                .title("problem1")
                .type("java")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        validUpdateQuestionRequest = QuestionRequest.builder()
                .title("problem1")
                .type("java")
                .build();

        InvalidUpdateQuestionRequest = QuestionRequest.builder()
                .title("problem2")
                .type("java")
                .build();


        validUpdateJsonRequest = objectMapper.writeValueAsString(validUpdateQuestionRequest);
        InvalidUpdateJsonRequest = objectMapper.writeValueAsString(InvalidUpdateQuestionRequest);

        given(questionService.updateQuestion(1L, validUpdateQuestionRequest)).willReturn(question);
        given(questionService.updateQuestion(1000L, InvalidUpdateQuestionRequest)).willThrow(new QuestionNotFoundException(1000L));
    }


    @Test
    @DisplayName("유효한 Question 업데이트 요청")
    void updateWithExistedQuestion() throws Exception{
        mockMvc.perform(put("/api/v1/question/"+1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUpdateJsonRequest))
                        .andDo(print())
                        .andExpect(status().isOk());

        verify(questionService).updateQuestion(eq(1L), any(QuestionRequest.class));
    }


    @Test
    @DisplayName("유효하지 않은 Question 업데이트 요청")
    void updateWithNotExistedQuestion() throws Exception{
        mockMvc.perform(put("/api/v1/question/"+1000L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(InvalidUpdateJsonRequest))
                        .andDo(print())
                        .andExpect(status().isNotFound());

         verify(questionService).updateQuestion(eq(1000L), any(QuestionRequest.class));
    }
}
