package com.example.interviewPrep.quiz.controller;

import com.example.interviewPrep.quiz.repository.QuizRepository;
import com.example.interviewPrep.quiz.service.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

// @ExtendWith와 @Mock을 통해서 Mock 객체를 생성했습니다.
// 이는 Mockito.mock 라이브러리를 활용하는 방식으로 대체 가능합니다.
@ExtendWith(MockitoExtension.class)
public class QuizControllerTest {

    @Mock
    QuizRepository quizRepository;
    QuizService quizService;
    QuizController quizController;

    @BeforeEach
    void setUp(){
        quizService = new QuizService(quizRepository);
        quizController = new QuizController(quizService);
    }

    @Test
    @DisplayName("퀴즈 리스트가 잘 호출되는지 테스트한다.")
    void list(){
        assertThat(quizController.list()).isEmpty();
    }
}
