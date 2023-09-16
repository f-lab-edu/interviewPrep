package com.example.interviewPrep.quiz.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final String accessToken;

    private final String refreshToken;

    @Builder
    public LoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static LoginResponse createLoginResponse(String accessToken, String refreshToken) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
