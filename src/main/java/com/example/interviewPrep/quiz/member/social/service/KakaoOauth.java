package com.example.interviewPrep.quiz.member.social.service;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.dto.Role;
import com.example.interviewPrep.quiz.member.social.dto.KakaoRes;
import com.example.interviewPrep.quiz.member.social.dto.SocialToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
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
public class KakaoOauth implements SocialOauth {

    @Value("${sns.kakao.url}")
    private String KAKAO_URL;
    @Value("${sns.kakao.client.id}")
    private String KAKAO_CLIENT_ID;
    @Value("${sns.kakao.client.secret}")
    private String KAKAO_CLIENT_SECRET;
    @Value("${sns.kakao.callback.url}")
    private String KAKAO_REDIRECT_URL;
    @Value("${sns.kakao.token.url}")
    private String KAKAO_TOKEN_URL;

    private WebClient webClient = WebClient.create();

    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", KAKAO_CLIENT_ID);
        params.put("redirect_uri", KAKAO_REDIRECT_URL);
        params.put("response_type", "code");
        params.put("scope", "profile_image, account_email, profile_nickname");


        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return KAKAO_URL + "?" + parameterString;
    }


    public Member getSocialMember(String code) {
        String token = requestAccessToken(code);
        return getSocialData(token);
    }


    public String requestAccessToken(String code) {

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("client_secret", KAKAO_CLIENT_SECRET);
        params.add("redirect_uri", KAKAO_REDIRECT_URL);
        params.add("grant_type", "authorization_code");

        return webClient.post()
                .uri(KAKAO_TOKEN_URL)
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new CommonException(FAILED_SOCIAL_LOGIN)))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new CommonException(FAILED_SOCIAL_LOGIN)))
                .bodyToMono(SocialToken.class)
                .block().getAccessToken();

    }



    public Member getSocialData(String accessToken) {
        KakaoRes kakaoRes = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new CommonException(FAILED_SOCIAL_LOGIN)))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new CommonException(FAILED_SOCIAL_LOGIN)))
                .bodyToMono(KakaoRes.class)
                .block();

        return  Member.builder()
                .name(kakaoRes.getProperties().getNickname())
                .nickName(kakaoRes.getProperties().getNickname())
                .email(kakaoRes.getKakaoAccount().getEmail())
                .picture(kakaoRes.getProperties().getProfileImage())
                .role(Role.USER) // 가입 시 기본 권한은 user(일반 사용자)
                .type("kakao")
                .build();

    }
    ///oauth/logout?client_id=${REST_API_KEY}&logout_redirect_uri=${LOGOUT_REDIRECT_URI}
}
