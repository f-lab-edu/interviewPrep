package com.example.interviewPrep.quiz.Member.service;

import com.example.interviewPrep.quiz.emitter.repository.EmitterService;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.dto.Role;
import com.example.interviewPrep.quiz.member.dto.request.LoginRequest;
import com.example.interviewPrep.quiz.member.dto.response.LoginResponse;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.member.service.AuthenticationService;
import com.example.interviewPrep.quiz.redis.RedisService;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {

    private AuthenticationService authService;
    private JwtUtil jwtUtil;
    private MemberRepository memberRepository;
    private EmitterService emitterService;
    private RedisService redisService;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {

        jwtUtil = mock(JwtUtil.class);
        memberRepository = mock(MemberRepository.class);
        emitterService = mock(EmitterService.class);
        redisService = mock(RedisService.class);
        response = mock(HttpServletResponse.class);

        authService = new AuthenticationService(jwtUtil, memberRepository, emitterService, redisService);
    }


    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {

        Long id = 1L;
        String email = "hello@gmail.com";
        String password = "1234";
        String hashedPassword = SHA256Util.encryptSHA256("1234");
        Role role = Role.USER;

        Member member = Member.builder()
                .id(id)
                .email(email)
                .password(hashedPassword)
                .role(role)
                .build();

        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
        when(jwtUtil.createAccessToken(member.getId(), member.getRole())).thenReturn("accessToken");
        when(jwtUtil.createRefreshToken(member.getId(), member.getRole())).thenReturn("refreshToken");

        LoginRequest loginRequest = new LoginRequest(email, password);
        LoginResponse loginResponse = authService.login(loginRequest, response);

        assertThat(loginResponse).isNotEqualTo(null);

    }


    @Test
    @DisplayName("로그인 실패 by 잘못된 이메일")
    void loginFailureByWrongEmail() {

        String rightEmail = "hello@gmail.com";
        String wrongEmail = "hello2@gmail.com";
        String password = "1234";
        String hashedPassword = SHA256Util.encryptSHA256(password);

        Member member = Member.builder()
                .email(rightEmail)
                .password(hashedPassword)
                .build();

        when(memberRepository.findByEmail(rightEmail)).thenReturn(Optional.of(member));
        when(jwtUtil.createAccessToken(member.getId(), member.getRole())).thenReturn("accessToken");
        when(jwtUtil.createRefreshToken(member.getId(), member.getRole())).thenReturn("refreshToken");

        LoginRequest loginRequest = new LoginRequest(wrongEmail, password);

        assertThatThrownBy(() -> authService.login(loginRequest, response))
                .isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("로그인 실패 by 잘못된 비밀번호")
    void loginFailureByWrongPassword() {

        String email = "hello@gmail.com";
        String rightPassword = "1234";
        String rightHashedPassword = SHA256Util.encryptSHA256(rightPassword);
        String wrongPassword = "5678";

        Member member = Member.builder()
                .email(email)
                .password(rightHashedPassword)
                .build();

        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));

        LoginRequest loginRequest = new LoginRequest(email, wrongPassword);

        assertThatThrownBy(() -> authService.login(loginRequest, response))
                .isInstanceOf(CommonException.class);

    }


}