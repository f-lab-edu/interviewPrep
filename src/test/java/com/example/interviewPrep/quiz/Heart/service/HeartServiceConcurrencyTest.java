package com.example.interviewPrep.quiz.Heart.service;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.heart.repository.AnswerLockRepository;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.heart.repository.HeartRepository;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.heart.service.HeartService;
import com.example.interviewPrep.quiz.question.domain.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class HeartServiceConcurrencyTest {
    @MockBean
    AnswerRepository answerRepository;

    @MockBean
    JwtService jwtService;
    @MockBean
    HeartRepository heartRepository;

    @MockBean
    AnswerLockRepository answerLockRepository;

    @MockBean
    MemberRepository memberRepository;

    HeartService heartService;

    @MockBean
    Question question;
    @MockBean
    Member member;
    Answer answer;

    @BeforeEach
    void setUp() {

        heartService = new HeartService(jwtService, heartRepository, answerLockRepository, answerRepository, memberRepository);

        answer = Answer.builder()
                .question(question)
                .member(member)
                .heartCnt(0)
                .build();

        answerRepository.save(answer);
        given(answerRepository.findById(1L)).willReturn(Optional.ofNullable(answer));
    }

    @Test
    @DisplayName("동시에 100개의 요청")
    public void increaseNamedLockTest() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    heartService.increaseAnswerHeartCntWithNamedLock(answer.getId());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        assertEquals(100, answerRepository.findById(answer.getId()).orElseThrow().getHeartCnt());

    }
}