package com.example.interviewPrep.quiz.Question.controller;

import com.example.interviewPrep.quiz.config.CustomAuthenticationEntryPoint;
import com.example.interviewPrep.quiz.filter.JwtAuthenticationFilter;
import com.example.interviewPrep.quiz.member.service.CustomOAuth2UserService;
import com.example.interviewPrep.quiz.question.controller.QuestionController;
import com.example.interviewPrep.quiz.question.dto.QuestionRequest;
import com.example.interviewPrep.quiz.question.service.QuestionService;
import com.example.interviewPrep.quiz.security.WithMockCustomOAuth2Account;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockCustomOAuth2Account()
@WebMvcTest(QuestionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class QuestionCreateWebControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    QuestionService questionService;

    @MockBean
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @MockBean
    CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    QuestionRequest validQuestionRequest;
    String validJsonRequest;

    @BeforeEach
    void setUp() throws Exception {

        validQuestionRequest = QuestionRequest.builder()
                .title("problem1")
                .type("java")
                .build();
    }


    @Test
    @DisplayName("유효한 Question 생성")
    void createWithValidAttributes() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        validJsonRequest = objectMapper.writeValueAsString(validQuestionRequest);

        mockMvc.perform(post("/api/v1/questions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest)
                )
                .andDo(print())
                .andExpect(status().isCreated());

        verify(questionService).createQuestion(any(QuestionRequest.class));
    }

}
