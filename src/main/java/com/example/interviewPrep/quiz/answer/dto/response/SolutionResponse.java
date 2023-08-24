package com.example.interviewPrep.quiz.answer.dto.response;

import lombok.*;

import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor
public class SolutionResponse {

    private Long answerId;
    private String answer;
    private String name;
    private int heartCnt;
    private int commentCnt;
    private boolean heart;
    private String createdDate;
    private String modifiedDate;
    private boolean modify;

    public SolutionResponse(Long answerId, String answer, String name, int heartCnt, int commentCnt, boolean heart, String createdDate, String modifiedDate, boolean modify) {

        Objects.requireNonNull(answerId, "answerId가 null입니다.");
        Objects.requireNonNull(answer, "answer가 null입니다.");
        Objects.requireNonNull(name, "name이 null입니다.");
        Objects.requireNonNull(createdDate, "createdDate가 null입니다.");
        Objects.requireNonNull(modifiedDate, "modifiedDate가 null입니다.");

        this.answerId = answerId;
        this.answer = answer;
        this.name = name;
        this.heartCnt = heartCnt;
        this.commentCnt = commentCnt;
        this.heart = heart;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.modify = modify;
    }
}
