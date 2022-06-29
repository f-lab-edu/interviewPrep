package com.example.interviewPrep.quiz.service;

import com.example.interviewPrep.quiz.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


class QuizServiceTest {

    private QuizService quizService;

    // mock 라이브러리를 활용해서 mock 객체를 생성했습니다.
    // 이를 통해 인터페이스만으로 (구현 클래스 없이) 객체를 생성할 수 있습니다.
    private QuizRepository quizRepository = mock(QuizRepository.class);


    @BeforeEach
    void setUp(){
        quizService = new QuizService(quizRepository);
    }

    @Test
    @DisplayName("퀴즈 목록이 잘 초기화하되는지 테스트한다.")
    void getQuiz(){
        given(quizRepository.init()).willReturn(List.of());
    }


}