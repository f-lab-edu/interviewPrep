package com.example.interviewPrep.quiz.member.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    MENTOR("ROLE_MENTOR", "멘토"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;
}
