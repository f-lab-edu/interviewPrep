package com.example.interviewPrep.quiz.Heart.service;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.heart.domain.Heart;
import com.example.interviewPrep.quiz.heart.dto.request.HeartRequest;
import com.example.interviewPrep.quiz.heart.exception.HeartNotFoundException;
import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.heart.repository.AnswerLockRepository;
import com.example.interviewPrep.quiz.heart.repository.HeartRepository;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.heart.service.HeartService;
import com.example.interviewPrep.quiz.question.domain.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HeartServiceTest {
    @Mock
    HeartRepository heartRepository;
    @Mock
    AnswerRepository answerRepository;

    @Mock
    AnswerLockRepository answerLockRepository;

    @Mock
    JwtService jwtService;

    @Mock
    MemberRepository memberRepository;

    HeartService heartService;

    Answer answer;

    @Mock
    Question question;

    @Mock
    Member member;

    @Mock
    Heart heart;

    @BeforeEach
    void setUp() {
        heartService = new HeartService(jwtService, heartRepository, answerLockRepository, answerRepository, memberRepository);
    }


    @Test
    @DisplayName("좋아요 테스트")
    void createHeart() {

        Answer answer = Answer.builder()
                .question(question)
                .member(member)
                .heartCnt(1)
                .build();

        HeartRequest heartRequest = HeartRequest.builder()
                                                .answerId(1L)
                                                .build();

        given(jwtService.getMemberId()).willReturn(1L);
        given(answerRepository.findById(1L)).willReturn(Optional.ofNullable(answer));
        given(memberRepository.findById(1L)).willReturn(Optional.ofNullable(member));
        given(heartRepository.findByAnswerIdAndMemberId(1L, 1L)).willReturn(Optional.empty());

        heartService.createHeart(heartRequest);

        assertThat(answer.getHeartCnt()).isEqualTo(2);
    }

    @Test
    @DisplayName("좋아요 취소 테스트")
    void delete(){

        Answer answer = Answer.builder()
                .question(question)
                .member(member)
                .heartCnt(1)
                .build();

        HeartRequest heartRequest = HeartRequest.builder()
                .answerId(1L)
                .build();

        given(jwtService.getMemberId()).willReturn(1L);
        given(answerRepository.findById(1L)).willReturn(Optional.ofNullable(answer));
        given(heartRepository.findByAnswerIdAndMemberId(1L, 1L)).willReturn(Optional.ofNullable(heart));

        heartService.deleteHeart(heartRequest);

        assertThat(answer.getHeartCnt()).isEqualTo(0);
    }

}
