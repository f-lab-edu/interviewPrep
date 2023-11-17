package com.example.interviewPrep.quiz.Answer.dto;

import com.example.interviewPrep.quiz.answer.dto.AnswerDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AnswerDTOTest {

    @Test
    @DisplayName("AnswerDTO 생성")
    void create(){
        AnswerDTO answer = AnswerDTO.builder()
                .id(1L)
                .content("답안입니다.")
                .build();

        assertThat(answer.getId()).isEqualTo(1L);
        assertThat(answer.getContent()).isEqualTo("답안입니다.");
    }
}
