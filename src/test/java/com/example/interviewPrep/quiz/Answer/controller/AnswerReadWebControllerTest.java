package com.example.interviewPrep.quiz.Answer.controller;

import com.example.interviewPrep.quiz.answer.controller.AnswerController;
import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.answer.dto.request.AnswerRequest;
import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.config.CustomAuthenticationEntryPoint;
import com.example.interviewPrep.quiz.exception.advice.CommonControllerAdvice;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.filter.JwtAuthenticationFilter;
import com.example.interviewPrep.quiz.heart.repository.HeartRepository;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.question.repository.QuestionRepository;
import com.example.interviewPrep.quiz.security.WithMockCustomOAuth2Account;
import com.example.interviewPrep.quiz.answer.service.AnswerService;
import com.example.interviewPrep.quiz.member.service.CustomOAuth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Optional;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_ANSWER;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AnswerController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(CommonControllerAdvice.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockCustomOAuth2Account()
public class AnswerReadWebControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AnswerService answerService;

    @MockBean
    AnswerRepository answerRepository;
    @MockBean
    MemberRepository memberRepository;

    @MockBean
    QuestionRepository questionRepository;

    @MockBean
    HeartRepository heartRepository;


    @MockBean
    CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private final JwtService jwtService = mock(JwtService.class);


    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    ObjectMapper objectMapper;
    String validJsonRequest;

    String invalidJsonRequest;


    @BeforeEach
    void setUp() throws Exception {

        answerService = new AnswerService(jwtService, memberRepository, answerRepository, questionRepository, heartRepository);

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
    @DisplayName("유효한 답안 읽기")
    void readValidAnswer() throws Exception{

        Member member = Member.builder()
                .id(1L)
                .email("abc@gmail.com")
                .name("abc")
                .password("1234")
                .build();

        Question question = Question.builder()
                .id(1L)
                .title("Question 1")
                .type("java")
                .difficulty("easy")
                .build();


        Answer answer = Answer.builder()
                        .content("hello")
                        .member(member)
                        .question(question)
                        .build();

        Long id = 1L;
        given(answerRepository.findById(id)).willReturn(Optional.ofNullable(answer));

        mockMvc.perform(get("/api/v1/answers/"+id))
                .andDo(print())
                .andExpect(status().isOk());

        verify(answerService).readAnswer(id);
    }


    @Test
    @DisplayName("유효하지 않은 답안 읽기")
    void readInValidAnswer() throws Exception{

        Member member = Member.builder()
                .id(1L)
                .email("abc@gmail.com")
                .name("abc")
                .password("1234")
                .build();

        Question question = Question.builder()
                .id(1L)
                .title("Question 1")
                .type("java")
                .difficulty("easy")
                .build();


        Answer answer = Answer.builder()
                .content("hello")
                .member(member)
                .question(question)
                .build();

        Long id = 2L;
        given(answerRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/answers/"+id))
                .andDo(print())
                .andExpect(status().isNotFound()) // Expecting a 404 status code
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verify response content type
                .andExpect(jsonPath("$.error").value("NOT_FOUND_ANSWER")) // Verify the error message
                .andReturn();

        verify(answerService).readAnswer(id);
    }




}
