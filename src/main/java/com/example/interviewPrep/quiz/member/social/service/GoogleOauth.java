package com.example.interviewPrep.quiz.member.social.service;


import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.dto.Role;
import com.example.interviewPrep.quiz.member.social.dto.GoogleRes;
import com.example.interviewPrep.quiz.member.social.dto.SocialToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleOauth implements SocialOauth {

    @Value("${sns.google.url}")
    private String GOOGLE_URL;
    @Value("${sns.google.client.id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${sns.google.callback.url}")
    private String GOOGLE_REDIRECT_URL;
    @Value("${sns.google.client.secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${sns.google.token.url}")
    private String GOOGLE_TOKEN_URL;

    private WebClient webClient = WebClient.create();

    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("scope", "profile%20email");
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_REDIRECT_URL);

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return GOOGLE_URL + "?" + parameterString;
    }


    public Member getSocialMember(String code) {
        String token = requestAccessToken(code);
        return getSocialData(token);
    }


    public String requestAccessToken(String code) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", GOOGLE_CLIENT_ID);
        params.add("client_secret", GOOGLE_CLIENT_SECRET);
        params.add("redirect_uri", GOOGLE_REDIRECT_URL);
        params.add("grant_type", "authorization_code");


        return webClient.post()
                .uri(GOOGLE_TOKEN_URL)
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new CommonException(FAILED_SOCIAL_LOGIN)))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new CommonException(FAILED_SOCIAL_LOGIN)))
                .bodyToMono(SocialToken.class)
                .block().getAccessToken();

    }


    public Member getSocialData(String accessToken) {
        GoogleRes googleRes = webClient.get()
                .uri("https://www.googleapis.com/userinfo/v2/me?access_token=" + accessToken)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new CommonException(FAILED_SOCIAL_LOGIN)))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new CommonException(FAILED_SOCIAL_LOGIN)))
                .bodyToMono(GoogleRes.class)
                .block();

        return Member.builder()
                .name(googleRes.getName())
                .nickName(googleRes.getName())
                .email(googleRes.getEmail())
                .picture(googleRes.getEmail())
                .role(Role.USER) // 가입 시 기본 권한은 user(일반 사용자)
                .type("google")
                .build();
    }
}

    //accounts.google.com/Logout?continue