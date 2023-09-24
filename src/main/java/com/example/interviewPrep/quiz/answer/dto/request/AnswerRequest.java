package com.example.interviewPrep.quiz.answer.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class AnswerRequest {

    private Long id;

    private Long questionId;

    private String content;

    @Builder
    public AnswerRequest(Long questionId, String content){
        Objects.requireNonNull(questionId, "questionId가 null입니다.");
        Objects.requireNonNull(content, "content가 null입니다.");

        this.id = id;
        this.questionId = questionId;
        this.content = content;
    }

}
