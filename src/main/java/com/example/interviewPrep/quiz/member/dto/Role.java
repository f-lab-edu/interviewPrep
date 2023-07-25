package com.example.interviewPrep.quiz.member.dto;

import lombok.Getter;

@Getter
public enum Role {

    MENTOR("ROLE_MENTOR", "멘토"),
    USER("ROLE_USER", "일반 사용자");

    private String key;
    private String title;

    Role(String key, String title) {
        this.key = key;
        this.title = title;
    }
}
