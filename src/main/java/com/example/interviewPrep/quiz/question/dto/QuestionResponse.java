package com.example.interviewPrep.quiz.question.dto;

import com.example.interviewPrep.quiz.question.domain.Question;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class QuestionResponse {

    private final Long id;
    private final String title;
    private final String type;
    private final String difficulty;
    private final boolean freeOfCharge;

    @Builder
    public QuestionResponse(Long id, String title, String type, String difficulty, boolean freeOfCharge) {
        Objects.requireNonNull(title, "title이 null입니다.");
        Objects.requireNonNull(type, "type이 null입니다.");

        this.id = id;
        this.title = title;
        this.type = type;
        this.difficulty = difficulty;
        this.freeOfCharge = freeOfCharge;
    }

    public static QuestionResponse createQuestionResponse(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .title(question.getTitle())
                .type(question.getType())
                .difficulty(question.getDifficulty())
                .freeOfCharge(question.isFreeOfCharge())
                .build();
    }
}
