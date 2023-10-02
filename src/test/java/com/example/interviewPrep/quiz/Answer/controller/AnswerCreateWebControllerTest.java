package com.example.interviewPrep.quiz.Answer.controller;

import com.example.interviewPrep.quiz.answer.controller.AnswerController;
import com.example.interviewPrep.quiz.answer.dto.request.AnswerRequest;
import com.example.interviewPrep.quiz.config.CustomAuthenticationEntryPoint;
import com.example.interviewPrep.quiz.filter.JwtAuthenticationFilter;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.security.WithMockCustomOAuth2Account;
import com.example.interviewPrep.quiz.answer.service.AnswerService;
import com.example.interviewPrep.quiz.member.service.CustomOAuth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AnswerController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockCustomOAuth2Account()
public class AnswerCreateWebControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AnswerService answerService;

    @MockBean
    CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @MockBean
    JwtService jwtService;

    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    ObjectMapper objectMapper;
    String validJsonRequest;

    String invalidJsonRequest;


    @BeforeEach
    void setUp() throws Exception {

        AnswerRequest validAnswerRequest = AnswerRequest.builder()
                                         .content("new answer")
                                         .questionId(2L)
                                         .build();


        AnswerRequest invalidAnswerRequest = AnswerRequest.builder()
                                         .content("new answer")
                                         .questionId(2L)
                                         .build();


        validJsonRequest = objectMapper.writeValueAsString(validAnswerRequest);
        invalidJsonRequest = objectMapper.writeValueAsString(invalidAnswerRequest);
    }


    @Test
    @DisplayName("유효한 답안 생성")
    void createValidAnswer() throws Exception{

        mockMvc.perform(post("/api/v1/answers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJsonRequest))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(answerService).createAnswer(any(AnswerRequest.class));
    }



}
