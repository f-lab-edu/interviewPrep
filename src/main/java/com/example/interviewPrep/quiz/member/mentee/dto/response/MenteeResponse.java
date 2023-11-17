package com.example.interviewPrep.quiz.member.mentee.dto.response;

import com.example.interviewPrep.quiz.member.mentee.domain.Mentee;
import lombok.Builder;

public class MenteeResponse {

    private final String email;
    private final String nickName;
    private final String name;


    @Builder
    public MenteeResponse(String email, String nickName, String name) {
        this.email = email;
        this.nickName = nickName;
        this.name = name;
    }

    public static MenteeResponse createMenteeResponse(Mentee mentee) {
        return MenteeResponse.builder()
                .email(mentee.getEmail())
                .nickName(mentee.getNickName())
                .name(mentee.getName())
                .build();
    }
}
