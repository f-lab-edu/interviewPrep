package com.example.interviewPrep.quiz.question.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestionResponse {

    private  Long id;
    private  String title;
    private  String type;
    private boolean status;
}
