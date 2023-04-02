package com.example.interviewPrep.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDto {

    private Long id;
    private String createDate;
    private String name;

}