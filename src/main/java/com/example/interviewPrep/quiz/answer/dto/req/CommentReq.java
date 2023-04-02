package com.example.interviewPrep.quiz.answer.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentReq {

    private Long answerId;

    private Long id;

    @NotBlank
    private  String comment;


}