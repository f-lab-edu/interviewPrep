package com.example.interviewPrep.quiz.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceDTO {

    private Long id;
    private String name;
    private Long questionId;
    @NotBlank
    private String link;
    private String createdDate;
    private String modifiedDate;
    private boolean modify;
    private int heartCnt;
    private boolean heart;
    private boolean myRef;

}