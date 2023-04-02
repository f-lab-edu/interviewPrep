package com.example.interviewPrep.quiz.Question.controller;

import com.example.interviewPrep.quiz.question.controller.QuestionController;
import com.example.interviewPrep.quiz.question.dto.QuestionDTO;
import com.example.interviewPrep.quiz.security.WithMockCustomOAuth2Account;
import com.example.interviewPrep.quiz.member.service.CustomOAuth2UserService;
import com.example.interviewPrep.quiz.question.service.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockCustomOAuth2Account()
@WebMvcTest(QuestionController.class)
public class QuestionCreateWebControllerTest {

    @MockBean
    QuestionService questionService;

    @MockBean
    CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    MockMvc mockMvc;

    QuestionDTO questionDTO;
    String jsonRequest;

    @BeforeEach
    void setUp() throws Exception{

        questionDTO = QuestionDTO.builder()
                .id(1L)
                .title("problem1")
                .type("java")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        jsonRequest = objectMapper.writeValueAsString(questionDTO);
    }


    @Test
    @DisplayName("Question 생성")
    void createWithValidAttributes() throws Exception{
        mockMvc.perform(post("/question")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(print())
                .andExpect(status().isOk());

        verify(questionService).createQuestion(any(QuestionDTO.class));
    }

}
