package com.example.interviewPrep.quiz.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final String accessToken;

    private final String refreshToken;

    private final boolean success;

    @Builder
    public LoginResponse(boolean success, String accessToken, String refreshToken) {
        this.success = success;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static LoginResponse createLoginResponse(boolean success, String accessToken, String refreshToken) {
        return LoginResponse.builder()
                .success(success)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
