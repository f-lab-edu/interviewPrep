package com.example.interviewPrep.quiz.answer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor
public class CommentResponse {

    private Long id;
    private String memberName;
    private String comment;
    private String createdDate;
    private String modifiedDate;
    private boolean modify;
    private boolean myAnswer;

    public CommentResponse(Long id, String memberName, String comment, String createdDate, String modifiedDate, boolean modify, boolean myAnswer) {

        Objects.requireNonNull(id, "id가 null입니다.");
        Objects.requireNonNull(memberName, "memberName이 null입니다.");
        Objects.requireNonNull(comment, "comment가 null입니다.");
        Objects.requireNonNull(createdDate, "createdDate가 null입니다.");
        Objects.requireNonNull(modifiedDate, "modifiedDate가 null입니다.");

        this.id = id;
        this.memberName = memberName;
        this.comment = comment;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.modify = modify;
        this.myAnswer = myAnswer;
    }
}