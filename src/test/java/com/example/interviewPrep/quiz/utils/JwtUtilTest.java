package com.example.interviewPrep.quiz.utils;

import com.example.interviewPrep.quiz.member.dto.Role;
import com.example.interviewPrep.quiz.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

class JwtUtilTest {

    private static final String SECRET = "12345678901234567890123456789010";
    private static final Long memberId = 1L;
    private static String accessToken;

    private static String refreshToken;
    private JwtUtil jwtUtil;

    private Role role;

    @BeforeEach
    void setUp(){
        jwtUtil = new JwtUtil(SECRET);
        accessToken = jwtUtil.createAccessToken(memberId, role);
        refreshToken = jwtUtil.createRefreshToken(memberId, role);
    }

    @Test
    void createAccesstoken() {
        assertThat(jwtUtil.isCreatedTokenValid(accessToken)).isTrue();
    }

    @Test
    void createRefreshtoken() {
        assertThat(jwtUtil.isCreatedTokenValid(refreshToken)).isTrue();
    }


    @Test
    void decodeWithValidToken() {
        Claims claims = jwtUtil.decode(accessToken);
        assertEquals(claims.get("memberId", Long.class), memberId);
    }


    @Test
    void decodeWithInValidToken() {
        assertThatThrownBy(() -> jwtUtil.decode(accessToken+"d"))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void decodeWithEmptyToken(){
        assertThatThrownBy(() -> jwtUtil.decode(null))
                .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> jwtUtil.decode(" "))
                .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> jwtUtil.decode("   "))
                .isInstanceOf(InvalidTokenException.class);

    }
}