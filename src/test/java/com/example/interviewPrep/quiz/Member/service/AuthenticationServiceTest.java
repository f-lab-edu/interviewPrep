package com.example.interviewPrep.quiz.Member.service;

import com.example.interviewPrep.quiz.emitter.repository.EmitterService;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.member.dto.request.LoginRequest;
import com.example.interviewPrep.quiz.member.dto.response.LoginResponse;
import com.example.interviewPrep.quiz.member.mentee.domain.Mentee;
import com.example.interviewPrep.quiz.member.mentee.repository.MenteeRepository;
import com.example.interviewPrep.quiz.member.mentor.domain.Mentor;
import com.example.interviewPrep.quiz.member.mentor.repository.MentorRepository;
import com.example.interviewPrep.quiz.member.service.AuthenticationService;
import com.example.interviewPrep.quiz.redis.RedisService;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AuthenticationServiceTest {

    private AuthenticationService authService;
    private JwtService jwtService;
    private MenteeRepository menteeRepository;

    private MentorRepository mentorRepository;
    private EmitterService emitterService;
    private RedisService redisService;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {

        jwtService = mock(JwtService.class);
        menteeRepository = mock(MenteeRepository.class);
        mentorRepository = mock(MentorRepository.class);
        emitterService = mock(EmitterService.class);
        redisService = mock(RedisService.class);
        response = mock(HttpServletResponse.class);

        authService = new AuthenticationService(jwtService, menteeRepository, mentorRepository, emitterService, redisService);
    }


    @Test
    @DisplayName("멘티 로그인 성공")
    void menteeLoginSuccess() {

        Long id = 1L;
        String email = "hello@gmail.com";
        String password = "1234";
        String hashedPassword = SHA256Util.encryptSHA256("1234");

        Mentee mentee = Mentee.builder()
                .email(email)
                .password(hashedPassword)
                .build();

        given(menteeRepository.findByEmail(email)).willReturn(Optional.of(mentee));
        given(jwtService.createAccessToken(1L, "Mentee")).willReturn("accessToken");
        given(jwtService.createRefreshToken(1L, "Mentee")).willReturn("refreshToken");

        LoginRequest loginRequest = new LoginRequest(email, password, "Mentee");
        LoginResponse loginResponse = authService.login(loginRequest, response);

        assertThat(loginResponse).isNotEqualTo(null);

    }


    @Test
    @DisplayName("멘티 로그인 실패 by 잘못된 이메일")
    void menteeLoginFailureByWrongEmail() {

        String rightEmail = "hello@gmail.com";
        String wrongEmail = "hello2@gmail.com";
        String password = "1234";
        String hashedPassword = SHA256Util.encryptSHA256(password);

        Mentee mentee = Mentee.builder()
                .email(rightEmail)
                .password(hashedPassword)
                .build();

        given(menteeRepository.findByEmail(rightEmail)).willReturn(Optional.of(mentee));
        given(jwtService.createAccessToken(1L, "Mentee")).willReturn("accessToken");
        given(jwtService.createRefreshToken(1L, "Mentee")).willReturn("refreshToken");

        LoginRequest loginRequest = new LoginRequest(wrongEmail, password, "Mentee");

        assertThatThrownBy(() -> authService.login(loginRequest, response))
                .isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("멘티 로그인 실패 by 잘못된 비밀번호")
    void menteeloginFailureByWrongPassword() {

        String email = "hello@gmail.com";
        String rightPassword = "1234";
        String rightHashedPassword = SHA256Util.encryptSHA256(rightPassword);
        String wrongPassword = "5678";

        Mentee mentee = Mentee.builder()
                .email(email)
                .password(rightHashedPassword)
                .build();

        given(menteeRepository.findByEmail(email)).willReturn(Optional.of(mentee));

        LoginRequest loginRequest = new LoginRequest(email, wrongPassword, "Mentee");

        assertThatThrownBy(() -> authService.login(loginRequest, response))
                .isInstanceOf(CommonException.class);

    }



    @Test
    @DisplayName("멘토 로그인 성공")
    void mentorLoginSuccess() {

        Long id = 1L;
        String email = "hello@gmail.com";
        String password = "1234";
        String hashedPassword = SHA256Util.encryptSHA256("1234");

        Mentor mentor = Mentor.builder()
                .email(email)
                .password(hashedPassword)
                .build();

        given(mentorRepository.findByEmail(email)).willReturn(Optional.of(mentor));
        given(jwtService.createAccessToken(1L, "Mentor")).willReturn("accessToken");
        given(jwtService.createRefreshToken(1L, "Mentor")).willReturn("refreshToken");

        LoginRequest loginRequest = new LoginRequest(email, password, "Mentor");
        LoginResponse loginResponse = authService.login(loginRequest, response);

        assertThat(loginResponse).isNotEqualTo(null);
    }


}