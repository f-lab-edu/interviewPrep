package com.example.interviewPrep.quiz.member.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class MemberRequest {

    private final String name;
    private final String nickName;
    private final String email;
    private final String password;
    private final String passwordConfirm;
    private final String type;
    private final String role;
    private String newPassword;

    @Builder
    public MemberRequest(String name, String nickName, String email, String password, String passwordConfirm, String type, String role) {
        Objects.requireNonNull(name, "name이 null입니다.");
        Objects.requireNonNull(nickName, "nickName이 null입니다.");
        Objects.requireNonNull(email, "email이 null입니다.");
        Objects.requireNonNull(password, "password가 null입니다.");
        Objects.requireNonNull(passwordConfirm, "passwordConfirm이 null입니다.");
        Objects.requireNonNull(type, "type이 null입니다.");
        Objects.requireNonNull(role, "role이 null입니다.");

        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.type = type;
        this.role = role;
    }
}
