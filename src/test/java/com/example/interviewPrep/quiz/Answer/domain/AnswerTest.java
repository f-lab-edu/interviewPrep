package com.example.interviewPrep.quiz.Answer.domain;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.question.domain.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class AnswerTest {

    @Mock
    Question question;

    @Mock
    Member member;


    @Test
    void createAnswer(){
        Answer answer = Answer.builder()
                        .question(question)
                        .member(member)
                        .content("답안입니다.")
                        .build();

        assertThat(answer.getContent()).isEqualTo("답안입니다.");
    }


    @Test
    void change(){
        Answer answer = Answer.builder()
                        .question(question)
                        .member(member)
                        .content("답안입니다.")
                        .build();

        answer.change("수정된 답안입니다.");

        assertThat(answer.getContent()).isEqualTo("수정된 답안입니다.");
    }





}
