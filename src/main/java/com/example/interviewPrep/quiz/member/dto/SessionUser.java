package com.example.interviewPrep.quiz.member.dto;

import com.example.interviewPrep.quiz.member.domain.Member;
import lombok.Getter;

//인증 된 사용자를 담는 클래스

@Getter
public class SessionUser  {
    private String name;
    private String email;
    private String picture;

    public SessionUser(Member user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
