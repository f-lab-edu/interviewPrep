package com.example.interviewPrep.quiz.member.mentee.dto.request;

import com.example.interviewPrep.quiz.member.mentee.domain.Mentee;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class MenteeRequest {

    private final String email;
    private final String password;
    private final String name;
    private final String nickName;
    private String newPassword;

    @Builder
    public MenteeRequest(String name, String nickName, String email, String password) {
        Objects.requireNonNull(name, "name이 null입니다.");
        Objects.requireNonNull(nickName, "nickName이 null입니다.");
        Objects.requireNonNull(email, "email이 null입니다.");
        Objects.requireNonNull(password, "password가 null입니다.");

        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }

    public static Mentee createMentee(MenteeRequest menteeRequest) {
        String password = SHA256Util.encryptSHA256(menteeRequest.password);

        return Mentee.builder()
                .name(menteeRequest.name)
                .nickName(menteeRequest.nickName)
                .email(menteeRequest.email)
                .password(password)
                .isPaid(false)
                .build();
    }
}
