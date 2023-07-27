package com.example.interviewPrep.quiz.Member.service;

import com.example.interviewPrep.quiz.emitter.repository.EmitterRepository;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.dto.LoginRequestDTO;
import com.example.interviewPrep.quiz.member.dto.LoginResponseDTO;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.member.service.AuthenticationService;
import com.example.interviewPrep.quiz.redis.RedisDao;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {

    private AuthenticationService authService;
    private JwtUtil jwtUtil;
    private MemberRepository memberRepository;
    private EmitterRepository emitterRepository;
    private RedisDao redisDao;
    private HttpServletResponse response;
    @BeforeEach
    void setUp(){

        jwtUtil = mock(JwtUtil.class);
        memberRepository = mock(MemberRepository.class);
        emitterRepository = mock(EmitterRepository.class);
        redisDao = mock(RedisDao.class);

        response = mock(HttpServletResponse.class);

        authService = new AuthenticationService(jwtUtil, memberRepository, emitterRepository, redisDao);
    }


    @Test
    @DisplayName("login success")
    void loginSuccess(){

        String email = "hello@gmail.com";
        String password = "1234";

        Member member = new Member(email, SHA256Util.encryptSHA256(password), "hello");
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(email, password);
        LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO, response);

        assertNotNull(loginResponseDTO, "Expected non-null LoginResponseDTO");

    }


    @Test
    @DisplayName("login failure by wrong email")
    void loginFailureByWrongEmail(){

        String rightEmail = "hello@gmail.com";
        String wrongEmail = "hello2@gmail.com";
        String password = "1234";

        Member member = new Member(rightEmail, SHA256Util.encryptSHA256(password), "hello");
        when(memberRepository.findByEmail(rightEmail)).thenReturn(Optional.of(member));

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(wrongEmail, password);

        assertThrows(CommonException.class, () -> authService.login(loginRequestDTO, response));

    }

    @Test
    @DisplayName("login failure by wrong password")
    void loginFailureByWrongPassword(){

        String email = "hello@gmail.com";
        String wrongPassword = "5678";
        String rightPassword = "1234";

        Member member = new Member(email, SHA256Util.encryptSHA256(rightPassword), "hello");
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(email, wrongPassword);

        assertThrows(CommonException.class, () -> authService.login(loginRequestDTO, response));

    }


}