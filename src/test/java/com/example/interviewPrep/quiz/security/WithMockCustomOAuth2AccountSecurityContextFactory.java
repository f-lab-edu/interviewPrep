package com.example.interviewPrep.quiz.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithMockCustomOAuth2AccountSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomOAuth2Account> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomOAuth2Account customOAuth2Account) {

        //1 securityContest 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // 2 유저 객체 정의
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("username", customOAuth2Account.username());
        attributes.put("name", customOAuth2Account.name());
        attributes.put("email", customOAuth2Account.email());
        attributes.put("picture", customOAuth2Account.picture());

        // 3 oauth2user 객체 생성
        OAuth2User principal = new DefaultOAuth2User(
                List.of(new OAuth2UserAuthority(customOAuth2Account.role(), attributes)),
                attributes,
                customOAuth2Account.name());

        // 4 인증 토큰 생성
        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(
                principal,
                principal.getAuthorities(),
                customOAuth2Account.registrationId());

        // 5 인증 토큰 담고 리턴턴
        context.setAuthentication(token);
        return context;
    }
}
