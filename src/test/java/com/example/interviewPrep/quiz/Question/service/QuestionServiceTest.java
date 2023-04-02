package com.example.interviewPrep.quiz.Question.service;

import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.question.dto.QuestionDTO;
import com.example.interviewPrep.quiz.question.repository.QuestionRepository;
import com.example.interviewPrep.quiz.question.service.QuestionService;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class QuestionServiceTest {

    private QuestionService questionService;

    private final QuestionRepository questionRepository = mock(QuestionRepository.class);
    private final AnswerRepository answerRepository = mock(AnswerRepository.class);

    Question question;

    @BeforeEach
    void setUp(){

        questionService = new QuestionService(questionRepository, answerRepository);

        question = Question.builder()
                            .id(1L)
                            .title("1번 문제")
                            .type("자바")
                            .build();

        given(questionRepository.findAll()).willReturn(List.of(question));
        given(questionRepository.findById(1L)).willReturn(Optional.of(question));
    }

    @Test
    @DisplayName("새 Question 생성")
    void createQuestion() {

        QuestionDTO questionDTO = QuestionDTO.builder()
                                    .id(10L)
                                    .title("자바 10번 문제")
                                    .type("자바")
                                    .build();

        Question createdQuestion = questionService.createQuestion(questionDTO);

        verify(questionRepository).save(any(Question.class));

        assertThat(createdQuestion.getId()).isEqualTo(10L);
        assertThat(createdQuestion.getTitle()).isEqualTo("자바 10번 문제");
        assertThat(createdQuestion.getType()).isEqualTo("자바");
    }

    @Test
    @DisplayName("유효한 ID로 Question 업데이트")
    void updateQuestionWithExistedId(){

       QuestionDTO questionDTO = QuestionDTO.builder()
                                .id(1L)
                                .title("문제 1번")
                                .type("자바")
                                .build();

       Question updatedQuestion = questionService.updateQuestion(questionDTO.getId(), questionDTO);

       assertThat(updatedQuestion.getId()).isEqualTo(1L);
       assertThat(updatedQuestion.getTitle()).isEqualTo("문제 1번");
    }

    @Test
    @DisplayName("Question 업데이트")
    void updateQuestionWithNotExistedId(){

        QuestionDTO questionDTO = QuestionDTO.builder()
                                .id(1000L)
                                .title("문제 1번")
                                .type("자바")
                                .build();

        assertThatThrownBy(() -> questionService.updateQuestion(questionDTO.getId(), questionDTO))
                .isInstanceOf(CommonException.class);

    }


    @Test
    @DisplayName("유효한 ID로 Question 삭제")
    void deleteQuestionWithExistedId(){

        questionService.deleteQuestion(1L);

        verify(questionRepository).delete(any(Question.class));
    }

    @Test
    @DisplayName("유효하지 않은 ID로 Question 삭제")
    void deleteQuestionWithNotExistedId(){

        assertThatThrownBy(() -> questionService.deleteQuestion(1000L))
                            .isInstanceOf(CommonException.class);

    }


    @Test
    void createPageQuestion(){
        List<QuestionDTO> questionDTOS = new ArrayList<>();

        QuestionDTO mydto;
        String[] type = {"java", "c++"};

        for(int i = 1; i<=40; i++) {
            mydto = QuestionDTO.builder()
                    .title("problem1")
                    .type(type[i%2])
                    .build();
            questionService.createQuestion(mydto);
            if (i%2==0)questionDTOS.add(mydto);
        }
    }

}
