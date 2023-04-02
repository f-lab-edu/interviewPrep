package com.example.interviewPrep.quiz.Question.domain;

import com.example.interviewPrep.quiz.question.domain.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionTest {

    @Test
    @DisplayName("Question을 Id 포함해서 생성")
    void creationWithId(){
        Question question = Question.builder()
                           .id(11L)
                           .title("String 클래스는 왜 불변입니까?")
                           .type("java")
                           .build();

        assertThat(question.getId()).isEqualTo(11L);
        assertThat(question.getTitle()).isEqualTo("String 클래스는 왜 불변입니까?");
        assertThat(question.getType()).isEqualTo("java");
    }


    @Test
    @DisplayName("Question을 Id 없이 생성")
    void creationWithoutId(){
        Question question = Question.builder()
                .title("String 클래스는 왜 불변입니까?")
                .type("java")
                .build();

        assertThat(question.getTitle()).isEqualTo("String 클래스는 왜 불변입니까?");
        assertThat(question.getType()).isEqualTo("java");
    }


    @Test
    void change(){
        Question question = Question.builder()
                .title("String 클래스는 왜 불변입니까?")
                .type("java")
                .build();

        question.change("자바 ArrayList에 대해 설명해보세요", "java");

        assertThat(question.getTitle()).isEqualTo("자바 ArrayList에 대해 설명해보세요");
        assertThat(question.getType()).isEqualTo("java");
    }

}