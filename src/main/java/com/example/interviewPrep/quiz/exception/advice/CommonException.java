package com.example.interviewPrep.quiz.exception.advice;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
    private ErrorCode error;
    private String message;
    public CommonException(ErrorCode error) {
        this.error = error;
    }

    public CommonException(ErrorCode error, String message) {
        this.error = error;
        this.message = message;
    }
}
