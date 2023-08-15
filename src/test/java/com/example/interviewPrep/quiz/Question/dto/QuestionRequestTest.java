package com.example.interviewPrep.quiz.Question.dto;

import com.example.interviewPrep.quiz.question.dto.QuestionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class QuestionRequestTest {

    @Test
    @DisplayName("QuestionRequest 생성")
    void create(){
        QuestionRequest questionRequest = QuestionRequest.builder()
                .id(1L)
                .title("problem1")
                .type("java")
                .build();

        assertThat(questionRequest.getId()).isEqualTo(1L);
        assertThat(questionRequest.getTitle()).isEqualTo("problem1");
        assertThat(questionRequest.getType()).isEqualTo("java");
    }


}