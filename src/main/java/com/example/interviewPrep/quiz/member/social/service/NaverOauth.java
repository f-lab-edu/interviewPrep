package com.example.interviewPrep.quiz.member.social.service;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.member.mentee.domain.Mentee;
import com.example.interviewPrep.quiz.member.social.dto.NaverRes;
import com.example.interviewPrep.quiz.member.social.dto.SocialToken;
import com.example.interviewPrep.quiz.utils.CreatePassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverOauth implements SocialOauth {

    private final CreatePassword createPassword;

    @Value("${security.oauth2.client.provider.naver.authorization_uri}")
    private String NAVER_URL;
    @Value("${security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;
    @Value("${security.oauth2.client.registration.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;
    @Value("${security.oauth2.client.registration.naver.redirect-uri}")
    private String NAVER_REDIRECT_URL;
    @Value("${security.oauth2.client.provider.naver.token-uri}")
    private String NAVER_TOKEN_URL;

    private WebClient webClient = WebClient.create();

    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", NAVER_CLIENT_ID);
        params.put("redirect_uri", NAVER_REDIRECT_URL);
        params.put("response_type", "code");
        params.put("state", new BigInteger(130, new SecureRandom()).toString());


        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return NAVER_URL + "?" + parameterString;
    }


    public Mentee getSocialMember(String code) {
        String token = requestAccessToken(code);
        return getSocialData(token);
    }

    public String requestAccessToken(String code) {

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", NAVER_CLIENT_ID);
        params.add("client_secret", NAVER_CLIENT_SECRET);
        params.add("grant_type", "authorization_code");
        params.add("state", new BigInteger(130, new SecureRandom()).toString());

        return webClient.post()
                .uri(NAVER_TOKEN_URL)
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new CommonException(FAILED_SOCIAL_LOGIN)))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new CommonException(FAILED_SOCIAL_LOGIN)))
                .bodyToMono(SocialToken.class)
                .block().getAccessToken();

    }


    public Mentee getSocialData(String accessToken){
         NaverRes naverRes =  webClient.get()
                .uri("https://openapi.naver.com/v1/nid/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new CommonException(FAILED_SOCIAL_LOGIN)))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new CommonException(FAILED_SOCIAL_LOGIN)))
                .bodyToMono(NaverRes.class)
                .block();

        String password = createPassword.createPwd();

        return  Mentee.builder()
                .name(naverRes.getResponse().getName())
                .nickName(naverRes.getResponse().getName())
                .email(naverRes.getResponse().getEmail())
                .password(password)
                .isPaid(false)
                .type("naver")
                .build();
    }

    //https://nid.naver.com/nidlogin.logout
}
