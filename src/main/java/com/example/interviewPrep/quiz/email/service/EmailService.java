package com.example.interviewPrep.quiz.email.service;

public interface EmailService {
    String sendSimpleMessage(String to, String type)throws Exception;
}
