package com.example.interviewPrep.quiz.Question.domain;

import com.example.interviewPrep.quiz.question.domain.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionTest {

    @Test
    @DisplayName("Question 생성")
    void creationWithId(){
        Question question = Question.builder()
                           .id(1L)
                           .title("problem1")
                           .type("java")
                           .difficulty("easy")
                           .build();

        assertThat(question.getId()).isEqualTo(1L);
        assertThat(question.getTitle()).isEqualTo("problem1");
        assertThat(question.getType()).isEqualTo("java");
        assertThat(question.getDifficulty()).isEqualTo("easy");
    }


    @Test
    @DisplayName("Question 필드값 변경")
    void change(){
        Question question = Question.builder()
                .id(1L)
                .title("problem1")
                .type("java")
                .difficulty("easy")
                .build();

        question.changeTitleOrType("problem2", "c++");

        assertThat(question.getTitle()).isEqualTo("problem2");
        assertThat(question.getType()).isEqualTo("c++");
        assertThat(question.getDifficulty()).isEqualTo("easy");
    }

}