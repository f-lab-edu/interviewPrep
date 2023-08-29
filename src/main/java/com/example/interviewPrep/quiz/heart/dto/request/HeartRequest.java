package com.example.interviewPrep.quiz.heart.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class HeartRequest {

    private Long answerId;

    @Builder
    public HeartRequest(Long answerId) {
        Objects.requireNonNull(answerId, "answerId가 null입니다.");
        this.answerId = answerId;
    }
}
