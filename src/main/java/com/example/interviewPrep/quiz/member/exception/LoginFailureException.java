package com.example.interviewPrep.quiz.member.exception;

public class LoginFailureException extends RuntimeException{
    public LoginFailureException(String message) {
        super(message);
    }
}
