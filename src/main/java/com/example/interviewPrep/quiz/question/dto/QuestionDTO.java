package com.example.interviewPrep.quiz.question.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {

    private  Long id;

    @NotNull
    private  String title;
    @NotNull
    private  String type;

    private boolean status;

}
