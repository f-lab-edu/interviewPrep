package com.example.interviewPrep.quiz.member.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseDTO {

    private String accessToken;

    private String refreshToken;
}
