package com.example.interviewPrep.quiz.answer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor
public class CommentRequest {

    private Long answerId;
    private Long id;
    private String comment;

    public CommentRequest(Long answerId, Long id, String comment){
        Objects.requireNonNull(answerId, "answerId가 null입니다.");
        Objects.requireNonNull(id, "id가 null입니다.");
        Objects.requireNonNull(comment, "comment가 null입니다.");

        this.answerId = answerId;
        this.id = id;
        this.comment = comment;
    }

}