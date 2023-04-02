package com.example.interviewPrep.quiz.Question.controller;

import com.example.interviewPrep.quiz.question.controller.QuestionController;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.question.dto.QuestionDTO;
import com.example.interviewPrep.quiz.question.exception.QuestionNotFoundException;
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

import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockCustomOAuth2Account()
@WebMvcTest(QuestionController.class)
public class QuestionUpdateWebControllerTest {

    @MockBean
    QuestionService questionService;

    @MockBean
    CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    MockMvc mockMvc;

    QuestionDTO validUpdateQuestionDTO;
    QuestionDTO InvalidUpdateQuestionDTO;

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


        validUpdateQuestionDTO = QuestionDTO.builder()
                .id(1L)
                .title("problem1")
                .type("java")
                .build();

        when(questionService.updateQuestion(1L, validUpdateQuestionDTO))
                .thenReturn(question);

        validUpdateJsonRequest = objectMapper.writeValueAsString(validUpdateQuestionDTO);




        InvalidUpdateQuestionDTO = QuestionDTO.builder()
                .id(1000L)
                .title("problem2")
                .type("java")
                .build();

        when(questionService.updateQuestion(1000L, InvalidUpdateQuestionDTO))
                .thenThrow(new QuestionNotFoundException(1000L));

        InvalidUpdateJsonRequest = objectMapper.writeValueAsString(InvalidUpdateQuestionDTO);


//        given(questionService.updateQuestion(eq(1L), any(QuestionDTO.class)))
//                .thenReturn(invocation -> {
//                    Long id = invocation.getArgument(0);
//                    QuestionDTO questionDTO = invocation.getArgument(1);
//                    return Question.builder()
//                            .id(id)
//                            .title(questionDTO.getTitle())
//                            .type(questionDTO.getType())
//                            .build();
//                });

    }


    @Test
    @DisplayName("request put valid question")
    void updateWithExistedQuestion() throws Exception{
        mockMvc.perform(put("/question/"+1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUpdateJsonRequest)
                )
                .andDo(print())
                .andExpect(status().isOk());

        verify(questionService).updateQuestion(eq(1L), any(QuestionDTO.class));
    }


//    @Test
//    @DisplayName("request put invalid question")
//    void updateWithNotExistedQuestion() throws Exception{
//        mockMvc.perform(put("/question/"+1000L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(InvalidUpdateJsonRequest)
//            )
//                      .andDo(print())
//                      .andExpect(status().isNoContent());
//
//       verify(questionService).updateQuestion(eq(1000L), any(QuestionDTO.class));
//    }
}
