package com.example.interviewPrep.quiz.Member.service;

import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.dto.LoginRequestDTO;
import com.example.interviewPrep.quiz.member.exception.LoginFailureException;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.member.repository.TokenRepository;
import com.example.interviewPrep.quiz.member.service.AuthenticationService;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class AuthenticationServiceTest {

    private static final String email = "hello@gmail.com";

    private static final String wrongEmail = "hello2@gmail.com";
    private static final String password = "1234";

    private static final String wrongPassword = "4321";
    private static final String SECRET = "12345678123456781234567812345678";
    private AuthenticationService authenticationService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TokenRepository tokenRepository;

    private LoginRequestDTO memberDTO;

    @BeforeEach
    void setUp(){
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(jwtUtil, memberRepository, tokenRepository);


        memberDTO = LoginRequestDTO.builder()
                .email(email)
                .password(password)
                .build();

        Member member = Member.builder()
                        .password(password)
                        .build();

        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));
    }


    @Test
    void loginWithWrongEmail(){

        assertThatThrownBy(
                () -> authenticationService.login(memberDTO)
        ).isInstanceOf(LoginFailureException.class);

        verify(memberRepository).findByEmail(email);
    }

    @Test
    void loginWithWrongPassword(){

        assertThatThrownBy(
                () -> authenticationService.login(memberDTO)
        ).isInstanceOf(LoginFailureException.class);

        verify(memberRepository).findByEmail(email);
    }
}