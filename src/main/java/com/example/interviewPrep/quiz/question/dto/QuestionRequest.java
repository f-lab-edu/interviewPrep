package com.example.interviewPrep.quiz.question.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class QuestionRequest {

    private final Long id;
    private final String title;
    private final String type;
    private final boolean status;

    @Builder
    public QuestionRequest(Long id, String title, String type, boolean status) {
        Objects.requireNonNull(id, "id가 null입니다.");
        Objects.requireNonNull(title, "title이 null입니다.");
        Objects.requireNonNull(type, "type이 null입니다.");

        this.id = id;
        this.title = title;
        this.type = type;
        this.status = status;
    }

}
