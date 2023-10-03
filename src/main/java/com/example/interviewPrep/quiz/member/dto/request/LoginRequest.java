package com.example.interviewPrep.quiz.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
public class LoginRequest {

    String email;
    String password;
    String type;

    public LoginRequest(String email, String password, String type) {
        this.email = email;
        this.password = password;
        this.type = type;
    }
}
