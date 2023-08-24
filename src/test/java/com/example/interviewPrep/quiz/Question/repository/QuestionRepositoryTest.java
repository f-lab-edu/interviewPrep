package com.example.interviewPrep.quiz.Question.repository;

import com.example.interviewPrep.quiz.config.CustomAuthenticationEntryPoint;
import com.example.interviewPrep.quiz.filter.JwtAuthenticationFilter;
import com.example.interviewPrep.quiz.member.controller.MemberController;
import com.example.interviewPrep.quiz.member.service.CustomOAuth2UserService;
import com.example.interviewPrep.quiz.member.social.service.GoogleOauth;
import com.example.interviewPrep.quiz.member.social.service.KakaoOauth;
import com.example.interviewPrep.quiz.member.social.service.NaverOauth;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.question.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class QuestionRepositoryTest {

    @MockBean
    MemberController memberController;
    @MockBean
    GoogleOauth googleOauth;
    @MockBean
    KakaoOauth kakaoOauth;
    @MockBean
    NaverOauth naverOauth;
    @MockBean
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @MockBean
    CustomOAuth2UserService customOAuth2UserService;
    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilter;
    Question question;
    @MockBean
    private QuestionRepository questionRepository;

    @BeforeEach
    public void setUp() {

        question = Question.builder()
                .id(1L)
                .title("problem1")
                .type("java")
                .build();

        given(questionRepository.save(question)).willReturn(question);
    }


    @Test
    @DisplayName("Question을 DB에 저장")
    public void save() {

        Question savedQuestion = questionRepository.save(question);
        Long savedId = savedQuestion.getId();
        assertThat(question.getId()).isEqualTo(savedId);
    }

    @Test
    @DisplayName("Question을 DB에서 삭제")
    public void delete() {

        // Given
        Question savedQuestion = questionRepository.save(question);
        // When
        questionRepository.delete(savedQuestion);
        Optional<Question> deletedQuestion = questionRepository.findById(savedQuestion.getId());

        // Then
        assertThat(deletedQuestion).isEqualTo(Optional.empty());
    }


    @Test
    @DisplayName("Question을 id로 검색")
    public void findById() {

        // Given
        Question savedQuestion = questionRepository.save(question);
        Long savedId = savedQuestion.getId();
        given(questionRepository.findById(savedId)).willReturn(Optional.of(savedQuestion));

        // When
        Optional<Question> findQuestion = questionRepository.findById(savedId);

        // Then
        assertThat(findQuestion).isPresent();
        assertThat(findQuestion.get()).isEqualTo(question);
    }
}
