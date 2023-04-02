package com.example.interviewPrep.quiz.answer.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRes {

    private  Long id;
    private  String memberName;
    private  String comment;
    private String createdDate;
    private String modifiedDate;
    private boolean modify;
    private boolean myAnswer;

}