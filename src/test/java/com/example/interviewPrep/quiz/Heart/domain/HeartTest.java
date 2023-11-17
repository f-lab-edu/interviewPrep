package com.example.interviewPrep.quiz.Heart.domain;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.heart.domain.Heart;
import com.example.interviewPrep.quiz.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

public class HeartTest {
    @MockBean
    Answer answer;
    Member member;

    @Test
    @DisplayName("좋아요 생성 테스트")
    void creationWithId() {
        Heart heart = Heart.builder()
            .id(1L)
            .answer(answer)
            .member(member)
            .build();
        assertThat(heart.getId()).isEqualTo(1L);
    }
}
