package com.example.interviewPrep.quiz.question.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@Builder
@RequiredArgsConstructor
public class FilterDTO {

    private final String language;

}
