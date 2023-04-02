package com.example.interviewPrep.quiz.member.social.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoRes {
    private Properties properties;
    private KakaoAccount kakaoAccount;


    @ToString
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Properties {
        private String profileImage;
        private String nickname;
    }

    @ToString
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoAccount {
        private String email;
    }



}
