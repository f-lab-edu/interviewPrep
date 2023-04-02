package com.example.interviewPrep.quiz.answer.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDTO {

    private Long id;

    @NotNull
    private Long questionId;

    @NotBlank
    private String content;

}