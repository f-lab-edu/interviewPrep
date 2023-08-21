package com.example.interviewPrep.quiz.interview.dto.request;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import lombok.Getter;

import java.util.List;

@Getter
public class InterviewRequest {
    private List<Answer> answers;
}
