package com.example.interviewPrep.quiz.Answer.dto;

import com.example.interviewPrep.quiz.answer.dto.request.AnswerRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AnswerRequestTest {

    @Test
    @DisplayName("AnswerRequest 생성")
    void create(){
        AnswerRequest answerRequest = AnswerRequest.builder()
                                                   .questionId(1L)
                                                   .content("답안입니다.")
                                                   .build();

        assertThat(answerRequest.getQuestionId()).isEqualTo(1L);
        assertThat(answerRequest.getContent()).isEqualTo("답안입니다.");
    }
}
