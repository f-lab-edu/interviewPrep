package com.example.interviewPrep.quiz.Answer.domain;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class AnswerTest {


    @Test
    void creationWithId(){
        Answer answer = Answer.builder()
                        .id(1L)
                        .content("답안입니다.")
                        .build();

        assertThat(answer.getId()).isEqualTo(1L);
        assertThat(answer.getContent()).isEqualTo("답안입니다.");
    }


    @Test
    void creationWithoutId(){
        Answer answer = Answer.builder()
                        .content("답안입니다2.")
                        .build();

        assertThat(answer.getContent()).isEqualTo("답안입니다2.");
    }


    @Test
    void change(){
        Answer answer = Answer.builder()
                        .content("답안입니다.")
                        .build();

        answer.change("수정된 답안입니다.");

        assertThat(answer.getContent()).isEqualTo("수정된 답안입니다.");
    }





}
