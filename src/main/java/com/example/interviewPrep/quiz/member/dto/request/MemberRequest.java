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
    private final String type;
    private final String role;
    private String newPassword;

    @Builder
    public MemberRequest(String name, String nickName, String email, String password, String type, String role) {
        Objects.requireNonNull(name, "name이 null입니다.");
        Objects.requireNonNull(nickName, "nickName이 null입니다.");
        Objects.requireNonNull(email, "email이 null입니다.");
        Objects.requireNonNull(password, "password가 null입니다.");
        Objects.requireNonNull(type, "type이 null입니다.");
        Objects.requireNonNull(role, "role이 null입니다.");

        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
        this.type = type;
        this.role = role;
    }

    public static Member createMember(MemberRequest memberRequest) {
        String password = SHA256Util.encryptSHA256(memberRequest.password);

        Role memberRole = Role.USER;
        if (memberRequest.role.equals("MENTOR")) {
            memberRole = Role.MENTOR;
        }

        return Member.builder()
                .name(memberRequest.name)
                .nickName(memberRequest.nickName)
                .email(memberRequest.email)
                .password(password)
                .type(memberRequest.type)
                .role(memberRole)
                .isPaid(false)
                .build();
    }
}