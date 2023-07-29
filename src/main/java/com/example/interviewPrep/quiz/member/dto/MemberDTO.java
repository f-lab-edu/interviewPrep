package com.example.interviewPrep.quiz.member.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class MemberDTO {

    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String password;
    @NotNull
    private String nickName;
    @NotNull
    private String newPassword;
    @NotNull
    private String type;

    public MemberDTO(Long id, String email, String password, String nickName, String newPassword, String type) {
        if (email == null || password == null || nickName == null || newPassword == null || type == null) {
            throw new NullPointerException("Required fields cannot be null");
        }
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.newPassword = newPassword;
        this.type = type;
    }



}
