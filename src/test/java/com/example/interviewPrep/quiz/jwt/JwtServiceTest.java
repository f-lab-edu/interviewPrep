package com.example.interviewPrep.quiz.jwt;

import com.example.interviewPrep.quiz.utils.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JwtServiceTest {

    private static final String SECRET = "12345678901234567890123456789010";
    @Autowired
    JwtService jwtService;


    @BeforeEach
    public void setUp(){
        jwtService = new JwtService(SECRET);
    }


    @Test
    @DisplayName("AccessToken 생성")
    public void createAccessToken(){

        Long memberId = 1L;

        String accessToken = jwtService.createAccessToken(memberId, "mentee");
        assertThat(accessToken.length()).isEqualTo(191);
    }

    @Test
    @DisplayName("RefreshToken 생성")
    public void createRefreshToken(){

        Long memberId = 1L;

        String refreshToken = jwtService.createRefreshToken(memberId, "mentee");
        assertThat(refreshToken.length()).isEqualTo(191);
    }


    @Test
    @DisplayName("AccessToken 복호화")
    public void decodeAccessToken(){

        Long memberId = 1L;

        String accessToken = jwtService.createAccessToken(memberId, "mentee");

        Claims claims = jwtService.decode(accessToken);
        assertThat(claims.get("id")).isEqualTo(Long.toString(memberId));
    }

    @Test
    @DisplayName("InvalidAccessToken 복호화")
    public void decodeInvalidAccessToken(){

        Long memberId = 1L;

        String accessToken = jwtService.createAccessToken(memberId, "mentee");

        assertThatThrownBy(() -> jwtService.decode(accessToken+"d")).isInstanceOf(io.jsonwebtoken.security.SignatureException.class);
    }

    @Test
    @DisplayName("EmptyToken 복호화")
    public void decodeEmptyToken(){
        assertThatThrownBy(() -> jwtService.decode(null)).isInstanceOf(java.lang.IllegalArgumentException.class);
        assertThatThrownBy(() -> jwtService.decode("")).isInstanceOf(java.lang.IllegalArgumentException.class);
    }

    @Test
    @DisplayName("RefreshToken 복호화")
    public void decodeRefreshToken(){
        Long memberId = 1L;
        String refreshToken = jwtService.createRefreshToken(memberId, "mentee");

        Claims claims = jwtService.decode(refreshToken);
        assertThat(claims.get("id")).isEqualTo(Long.toString(memberId));
    }


}
