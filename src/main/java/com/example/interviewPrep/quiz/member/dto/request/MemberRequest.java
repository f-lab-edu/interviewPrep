package com.example.interviewPrep.quiz.member.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class MemberRequest {

    private final String email;
    private final String password;
    private final String passwordConfirm;
    private final String nickName;
    private final String type;
    private String newPassword;

    @Builder
    public MemberRequest(String email, String password, String passwordConfirm, String nickName, String type) {
        Objects.requireNonNull(email, "email이 null입니다.");
        Objects.requireNonNull(password, "password가 null입니다.");
        Objects.requireNonNull(passwordConfirm, "passwordConfirm이 null입니다.");
        Objects.requireNonNull(nickName, "nickName이 null입니다.");
        Objects.requireNonNull(type, "type이 null입니다.");

        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.nickName = nickName;
        this.type = type;
    }
}
