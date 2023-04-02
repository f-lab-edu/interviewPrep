package com.example.interviewPrep.quiz.Question.dto;

import com.example.interviewPrep.quiz.question.dto.QuestionDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class QuestionDTOTest {

    @Test
    @DisplayName("QuestionDTO 생성")
    void create(){
        QuestionDTO question = QuestionDTO.builder()
                .id(1L)
                .title("problem1")
                .build();

        assertThat(question.getId()).isEqualTo(1L);
        assertThat(question.getTitle()).isEqualTo("problem1");
    }


}