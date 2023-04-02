package com.example.interviewPrep.quiz.exception.advice;

import lombok.Getter;

@Getter
public class LoginException extends RuntimeException {
    private final ErrorCode error;

    public LoginException(ErrorCode error) {
        this.error = error;
    }
}
