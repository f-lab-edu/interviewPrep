package com.example.interviewPrep.quiz.member.dto.request;

import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.dto.Role;
import com.example.interviewPrep.quiz.utils.SHA256Util;
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
    private String newPassword;

    @Builder
    public MemberRequest(String name, String nickName, String email, String password, String passwordConfirm, String type, String role) {
        Objects.requireNonNull(name, "name이 null입니다.");
        Objects.requireNonNull(nickName, "nickName이 null입니다.");
        Objects.requireNonNull(email, "email이 null입니다.");
        Objects.requireNonNull(password, "password가 null입니다.");
        Objects.requireNonNull(passwordConfirm, "passwordConfirm이 null입니다.");
        Objects.requireNonNull(type, "type이 null입니다.");

        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.type = type;
    }

    public static Member createMember(MemberRequest memberRequest) {
        String password = SHA256Util.encryptSHA256(memberRequest.password);

        return Member.builder()
                .name(memberRequest.name)
                .nickName(memberRequest.nickName)
                .email(memberRequest.email)
                .password(password)
                .isPaid(false)
                .build();
    }
}
