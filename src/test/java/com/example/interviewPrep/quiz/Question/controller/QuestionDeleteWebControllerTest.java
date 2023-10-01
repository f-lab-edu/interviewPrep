package com.example.interviewPrep.quiz.Question.controller;

import com.example.interviewPrep.quiz.config.CustomAuthenticationEntryPoint;
import com.example.interviewPrep.quiz.exception.advice.ErrorCode;
import com.example.interviewPrep.quiz.filter.JwtAuthenticationFilter;
import com.example.interviewPrep.quiz.member.service.CustomOAuth2UserService;
import com.example.interviewPrep.quiz.question.controller.QuestionController;
import com.example.interviewPrep.quiz.question.domain.Question;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockCustomOAuth2Account()
@WebMvcTest(QuestionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class QuestionDeleteWebControllerTest {

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

    QuestionRequest questionRequest;
    String jsonRequest;

    @BeforeEach
    void setUp() throws Exception {

        questionRequest = QuestionRequest.builder()
                .title("problem1")
                .type("java")
                .build();

        Question question = Question.builder()
                .title("problem1")
                .type("java")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        jsonRequest = objectMapper.writeValueAsString(questionRequest);

        given(questionService.deleteQuestion(1L)).willReturn(question);
        given(questionService.deleteQuestion(1000L)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                ErrorCode.NOT_FOUND_QUESTION.getMessage(1000L)));
    }


    @Test
    @DisplayName("유효한 Question 삭제")
    void deleteQuestion() throws Exception {
        long id = 1L;
        mockMvc.perform(delete("/api/v1/questions/" + id))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(questionService).deleteQuestion(1L);
    }


    @Test
    @DisplayName("유효하지 않은 Question 삭제")
    void deleteQuestionWithNotExistedId() throws Exception {
        mockMvc.perform(delete("/api/v1/questions/" + 1000L))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(questionService).deleteQuestion(1000L);
    }

}
