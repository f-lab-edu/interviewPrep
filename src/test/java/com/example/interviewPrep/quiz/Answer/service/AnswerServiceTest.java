package com.example.interviewPrep.quiz.Answer.service;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.answer.dto.response.AnswerResponse;
import com.example.interviewPrep.quiz.heart.repository.HeartRepository;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.answer.dto.request.AnswerRequest;
import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.question.repository.QuestionRepository;
import com.example.interviewPrep.quiz.answer.service.AnswerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


public class AnswerServiceTest {

    @Autowired
    private AnswerService answerService;

    private final JwtService jwtService = mock(JwtService.class);

    private final AnswerRepository answerRepository = mock(AnswerRepository.class);
    private final QuestionRepository questionRepository =  mock(QuestionRepository.class);
    private final MemberRepository memberRepository =  mock(MemberRepository.class);
    private final HeartRepository heartRepository = mock(HeartRepository.class);

    Answer answer;

    @BeforeEach
    public void setUp(){
        answerService = new AnswerService(jwtService, memberRepository, answerRepository, questionRepository, heartRepository);
    }


    @Test
    @DisplayName("답안 생성")
    public void createAnswer(){

        //given
        AnswerRequest answerRequest = AnswerRequest.builder()
                        .content("hello")
                        .questionId(1L)
                        .build();

        Member member = Member.builder()
                .id(1L)
                .email("abc@gmail.com")
                .name("abc")
                .password("1234")
                .build();

        Question question = Question.builder()
                   .id(1L)
                   .title("Question 1")
                   .type("java")
                   .difficulty("easy")
                   .build();

        given(jwtService.getMemberId()).willReturn(1L);
        given(memberRepository.findById(1L)).willReturn(Optional.ofNullable(member));
        given(questionRepository.findById(answerRequest.getId())).willReturn(Optional.ofNullable(question));


        answer = Answer.builder()
                .content(answerRequest.getContent())
                .member(member)
                .question(question)
                .build();

        verify(answerRepository, times(0)).save(answer);

        //when
        AnswerResponse answerResponse = answerService.createAnswer(answerRequest);

        //then
        assertThat(answerResponse.getContent()).isEqualTo("hello");

    }


    @Test
    @DisplayName("답안 읽기")
    public void readAnswer(){

        Member member = Member.builder()
                .id(1L)
                .email("abc@gmail.com")
                .name("abc")
                .password("1234")
                .build();

        Question question = Question.builder()
                .id(1L)
                .title("Question 1")
                .type("java")
                .difficulty("easy")
                .build();

        answer = Answer.builder()
                .content("hello")
                .member(member)
                .question(question)
                .build();

        given(answerRepository.findById(1L)).willReturn(Optional.ofNullable(answer));

        AnswerResponse answerResponse = answerService.readAnswer(1L);

        assertThat(answerResponse.getContent()).isEqualTo("hello");

    }


    @Test
    @DisplayName("답안 업데이트")
    public void updateAnswer() {

        Member member = Member.builder()
                .id(1L)
                .email("abc@gmail.com")
                .name("abc")
                .password("1234")
                .build();

        Question question = Question.builder()
                .id(1L)
                .title("Question 1")
                .type("java")
                .difficulty("easy")
                .build();

        answer = Answer.builder()
                .content("hello")
                .member(member)
                .question(question)
                .build();

        AnswerRequest answerRequest = AnswerRequest.builder()
                                    .questionId(1L)
                                    .content("hello2")
                                    .build();

        given(jwtService.getMemberId()).willReturn(1L);
        given(answerRepository.findById(1L)).willReturn(Optional.ofNullable(answer));

        AnswerResponse answerResponse = answerService.updateAnswer(1L, answerRequest);

        assertThat(answerResponse.getContent()).isEqualTo("hello2");
    }


    @Test
    @DisplayName("답안 삭제")
    public void deleteAnswer(){

        Member member = Member.builder()
                .id(1L)
                .email("abc@gmail.com")
                .name("abc")
                .password("1234")
                .build();

        Question question = Question.builder()
                .id(1L)
                .title("Question 1")
                .type("java")
                .difficulty("easy")
                .build();

        answer = Answer.builder()
                .content("hello")
                .member(member)
                .question(question)
                .build();

        given(jwtService.getMemberId()).willReturn(1L);
        given(answerRepository.findById(1L)).willReturn(Optional.ofNullable(answer));

        answerService.deleteAnswer(1L);

        verify(answerRepository).delete(any(Answer.class));

    }

}
