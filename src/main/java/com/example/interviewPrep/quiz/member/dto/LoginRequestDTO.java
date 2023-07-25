package com.example.interviewPrep.quiz.member.dto;

import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
public class LoginRequestDTO {

    String email;
    String password;
}
