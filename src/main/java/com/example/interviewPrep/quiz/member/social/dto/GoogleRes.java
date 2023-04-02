package com.example.interviewPrep.quiz.member.social.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleRes {
    private String name;
    private String email;
    private String picture;
}
