package com.example.interviewPrep.quiz.member.mentee.dto.request;

import com.example.interviewPrep.quiz.company.domain.Company;
import com.example.interviewPrep.quiz.member.mentee.domain.Mentee;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class MenteeRequest {

    private final String name;
    private final String nickName;
    private final String email;
    private final String companyName;
    private final String password;
    private String newPassword;
    private final String picture;
    private final boolean isPaid;
    private final String type;

    @Builder
    public MenteeRequest(String name, String nickName, String email, String companyName, String password, Long company_id, String picture, boolean isPaid, String type) {
        Objects.requireNonNull(name, "name이 null입니다.");
        Objects.requireNonNull(nickName, "nickName이 null입니다.");
        Objects.requireNonNull(email, "email이 null입니다.");
        Objects.requireNonNull(companyName, "companyName이 null입니다.");
        Objects.requireNonNull(password, "password가 null입니다.");
        Objects.requireNonNull(type, "type이 null입니다.");

        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.companyName = companyName;
        this.password = password;
        this.picture = picture;
        this.isPaid = isPaid;
        this.type = type;
    }

    public static Mentee createMentee(MenteeRequest menteeRequest) {
        String password = SHA256Util.encryptSHA256(menteeRequest.getPassword());

        return Mentee.builder()
                .name(menteeRequest.getName())
                .nickName(menteeRequest.getNickName())
                .email(menteeRequest.getEmail())
                .picture(menteeRequest.getPicture())
                .password(password)
                .type(menteeRequest.getType())
                .isPaid(false)
                .build();
    }
}
