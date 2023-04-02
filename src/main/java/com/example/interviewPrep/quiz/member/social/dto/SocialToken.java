package com.example.interviewPrep.quiz.member.social.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SocialToken {

    @JsonProperty("access_token")
    private String accessToken;

}
