package com.example.interviewPrep.quiz.Heart.domain;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.heart.domain.Heart;
import com.example.interviewPrep.quiz.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class HeartTest {
    @Mock
    Answer answer;
    @Mock
    Member member;

    @Test
    @DisplayName("좋아요 생성 테스트")
    void creationWithId() {

        Heart heart = Heart.builder()
            .answer(answer)
            .member(member)
            .build();

        assertThat(heart.getAnswer()).isEqualTo(answer);
        assertThat(heart.getMember()).isEqualTo(member);
    }
}
