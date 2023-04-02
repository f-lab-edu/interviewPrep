package com.example.interviewPrep.quiz.utils;

public class PasswordCheck {

    public static boolean isMatch(String savedPassword, String password){
        return savedPassword.equals(password);
    }
}

