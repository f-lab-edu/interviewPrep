package com.example.interviewPrep.quiz.answer.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor
public class AnswerResponse {

    private Long id;
    private Long questionId;
    private String content;
    private String createdDate;
    private String name;

    public AnswerResponse(Long id, Long questionId, String content, String createdDate, String name){
        Objects.requireNonNull(id, "id가 null입니다.");
        Objects.requireNonNull(questionId, "questionId가 null입니다.");
        Objects.requireNonNull(content, "content가 null입니다.");
        Objects.requireNonNull(createdDate, "createdDate가 null입니다.");
        Objects.requireNonNull(name, "name이 null입니다.");

        this.id = id;
        this.questionId = questionId;
        this.content = content;
        this.createdDate = createdDate;
        this.name = name;
    }
}
