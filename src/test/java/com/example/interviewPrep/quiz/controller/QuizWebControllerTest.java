package com.example.interviewPrep.quiz.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


// @SpringBootTest와 @AutoConfigureMockMvc를
// @WebMvcTest 대신 사용하였습니다.
@SpringBootTest
@AutoConfigureMockMvc
class QuizWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void list() throws Exception{
        mockMvc.perform(get("/quizes"))
                .andExpect(status().isOk());
    }

}