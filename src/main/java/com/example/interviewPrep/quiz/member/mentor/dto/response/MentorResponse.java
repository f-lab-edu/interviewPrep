package com.example.interviewPrep.quiz.member.mentor.dto.response;

import com.example.interviewPrep.quiz.member.mentor.domain.Mentor;
import lombok.Builder;

public class MentorResponse {

    private final String email;
    private final String nickName;
    private final String name;


    @Builder
    public MentorResponse(String email, String nickName, String name) {
        this.email = email;
        this.nickName = nickName;
        this.name = name;
    }

    public static MentorResponse createMentorResponse(Mentor mentor) {
        return MentorResponse.builder()
                .email(mentor.getEmail())
                .nickName(mentor.getNickName())
                .name(mentor.getName())
                .build();
    }
}
