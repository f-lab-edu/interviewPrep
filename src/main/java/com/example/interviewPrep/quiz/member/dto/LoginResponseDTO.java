package com.example.interviewPrep.quiz.member.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class LoginResponseDTO {

    private String accessToken;

    private String refreshToken;
}
