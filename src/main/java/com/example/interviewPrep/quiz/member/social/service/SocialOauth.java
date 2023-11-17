package com.example.interviewPrep.quiz.member.social.service;

import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.mentee.domain.Mentee;

public interface SocialOauth {

    String getOauthRedirectURL();
    String requestAccessToken(String code);
    Mentee getSocialMember(String code);
    Mentee getSocialData(String token);
}
