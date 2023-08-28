package com.example.interviewPrep.quiz.question.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class QuestionRequest {

    private final String title;
    private final String type;
    private final String difficulty;
    private final boolean freeOfCharge;

    @Builder
    public QuestionRequest(String title, String type, String difficulty, boolean freeOfCharge) {
        Objects.requireNonNull(title, "title이 null입니다.");
        Objects.requireNonNull(type, "type이 null입니다.");
        Objects.requireNonNull(difficulty, "difficulty가 null입니다.");

        this.title = title;
        this.type = type;
        this.difficulty = difficulty;
        this.freeOfCharge = freeOfCharge;
    }

}
