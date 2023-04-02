package com.example.interviewPrep.quiz.member.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

}
