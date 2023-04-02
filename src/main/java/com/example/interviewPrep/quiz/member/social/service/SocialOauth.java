package com.example.interviewPrep.quiz.member.social.service;

import com.example.interviewPrep.quiz.member.domain.Member;

public interface SocialOauth {

    String getOauthRedirectURL();
    String requestAccessToken(String code);
    Member getSocialMember(String code);
    Member getSocialData(String token);
}
