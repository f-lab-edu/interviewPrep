package com.example.interviewPrep.quiz.Answer.repository;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest
class AnswerRepositoryTest {
    @Autowired
    private final AnswerRepository answerRepository = mock(AnswerRepository.class);

    @Test
    @DisplayName("Answer를 DB에 저장")
    public void save(){

        // Given
        Answer answer1 = Answer.builder()
                .content("답안1")
                .build();

        // When
        Answer savedAnswer = answerRepository.save(answer1);

        // Then
        assertEquals(savedAnswer.getId(), answer1.getId());
    }


}
