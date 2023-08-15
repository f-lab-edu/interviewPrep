package com.example.interviewPrep.quiz.answer.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Builder
public class AnswersResponse {
    public final List<AnswerResponse> answerResponses;

    public AnswersResponse(List<AnswerResponse> answerResponses){
        this.answerResponses = answerResponses;
    }
}
