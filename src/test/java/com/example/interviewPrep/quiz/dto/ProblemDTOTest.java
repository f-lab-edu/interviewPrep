package com.example.interviewPrep.quiz.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ProblemDTOTest {

    @Test
    void create(){
        ProblemDTO problem = ProblemDTO.builder()
                .id(1L)
                .title("problem1")
                .build();

        assertThat(problem.getId()).isEqualTo(1L);
        assertThat(problem.getTitle()).isEqualTo("problem1");
    }

}