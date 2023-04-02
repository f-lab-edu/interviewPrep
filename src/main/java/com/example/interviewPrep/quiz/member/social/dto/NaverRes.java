package com.example.interviewPrep.quiz.member.social.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NaverRes {

    private response response;

    @ToString
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class response {
        String name;
        String email;
        @JsonProperty("profile_image")
        String profileImage;
    }
}
