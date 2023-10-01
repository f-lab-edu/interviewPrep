package com.example.interviewPrep.quiz.Heart.service;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.heart.dto.request.HeartRequest;
import com.example.interviewPrep.quiz.heart.repository.AnswerLockRepository;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.heart.repository.HeartRepository;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.heart.service.HeartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class HeartServiceConcurrencyTest {
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    AnswerLockRepository answerLockRepository;
    @Autowired
    HeartRepository heartRepository;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtService jwtService;

    HeartService heartService;

    Answer answer;
    Member member;

    @BeforeEach
    void setUp() {
        answer = Answer.builder()
            .build();
        member = Member.builder()
            .email("test@gmail.com")
            .build();
        answerRepository.save(answer);
        memberRepository.save(member);

        heartService = new HeartService(jwtService, heartRepository, answerLockRepository, answerRepository, memberRepository);
    }

    @Test
    void increaseOptimisticLockTest() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    heartService.createHeart(HeartRequest.builder()
                            .answerId(answer.getId()).build());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        assertEquals(100, answerRepository.findById(answer.getId()).orElseThrow().getHeartCnt());

    }
}
