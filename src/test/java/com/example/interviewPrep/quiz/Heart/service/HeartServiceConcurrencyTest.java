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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class HeartServiceConcurrencyTest {
    @Mock
    AnswerRepository answerRepository;

    @Mock
    JwtService jwtService;

    @Mock
    HeartRepository heartRepository;

    @Mock
    AnswerLockRepository answerLockRepository;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    @Spy
    HeartService heartService;

    @Mock
    Question question;
    @Mock
    Member member;
    Answer answer;

    @BeforeEach
    void setUp() {

        // heartService = new HeartService(jwtService, heartRepository, answerLockRepository, answerRepository, memberRepository);

        answer = Answer.builder()
                .question(question)
                .member(member)
                .heartCnt(0)
                .build();

        answerRepository.save(answer);
        given(answerRepository.findById(1L)).willReturn(Optional.ofNullable(answer));
    }

    /*
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

        assertEquals(100, answer.getHeartCnt());

    }
    */
}