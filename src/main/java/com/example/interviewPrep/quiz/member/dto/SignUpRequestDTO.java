package com.example.interviewPrep.quiz.member.dto;

import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDTO {

    @NotBlank
    private String nickName;
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordConfirm;

    @NotBlank
    private String code;
    private Role role;

}
