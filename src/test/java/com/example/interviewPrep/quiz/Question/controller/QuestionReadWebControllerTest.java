package com.example.interviewPrep.quiz.Question.controller;

import com.example.interviewPrep.quiz.config.CustomAuthenticationEntryPoint;
import com.example.interviewPrep.quiz.filter.JwtAuthenticationFilter;
import com.example.interviewPrep.quiz.member.service.CustomOAuth2UserService;
import com.example.interviewPrep.quiz.question.controller.QuestionController;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.question.dto.QuestionResponse;
import com.example.interviewPrep.quiz.question.service.QuestionService;
import com.example.interviewPrep.quiz.security.WithMockCustomOAuth2Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockCustomOAuth2Account()
@WebMvcTest(QuestionController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
public class QuestionReadWebControllerTest {

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
    Question question;
    Pageable pageable;

    @BeforeEach
    void setUp() {

        question = Question.builder()
                .id(1L)
                .title("problem1")
                .type("java")
                .difficulty("easy")
                .build();

        QuestionResponse singleQuestionResponse = QuestionResponse.builder()
                .id(1L)
                .title("problem1")
                .type("java")
                .difficulty("easy")
                .build();


        List<QuestionResponse> questionResponses = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Long id = (long) (i + 1);
            String title = "problem" + Integer.toString(i + 1);

            QuestionResponse questionResponse = QuestionResponse.builder()
                    .id(id)
                    .title(title)
                    .type("java")
                    .build();

            questionResponses.add(questionResponse);
        }
        pageable = PageRequest.of(0, 10);
        Page<QuestionResponse> pageQuestionResponses = new PageImpl<>(questionResponses, pageable, 10);

        given(questionService.getQuestion(1L)).willReturn(singleQuestionResponse);
        given(questionService.findByType("java", pageable)).willReturn(pageQuestionResponses);
    }


    @Test
    @DisplayName("Question 검색")
    void getQuestion() throws Exception {

        long id = 1L;
        mockMvc.perform(get("/api/v1/questions/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Question을 타입별로 검색")
    void getQuestionsByType() throws Exception {

        String type = "java";

        mockMvc.perform(get("/api/v1/questions/by-type/" + type)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


}