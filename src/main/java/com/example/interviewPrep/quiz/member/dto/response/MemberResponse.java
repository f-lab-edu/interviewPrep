package com.example.interviewPrep.quiz.member.dto.response;

import com.example.interviewPrep.quiz.member.domain.Member;
import lombok.Builder;

public class MemberResponse {

    private final String email;
    private final String nickName;
    private final String name;


    @Builder
    public MemberResponse(String email, String nickName, String name) {
        this.email = email;
        this.nickName = nickName;
        this.name = name;
    }

    public static MemberResponse createMemberResponse(Member member) {
        return MemberResponse.builder()
                .email(member.getEmail())
                .nickName(member.getNickName())
                .name(member.getName())
                .build();
    }
}
