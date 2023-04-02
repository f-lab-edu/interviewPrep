package com.example.interviewPrep.quiz.exception.advice;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
    private final ErrorCode error;

    public CommonException(ErrorCode error) {
        this.error = error;
    }
}
