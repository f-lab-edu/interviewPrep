package com.example.interviewPrep.quiz.member.social.service;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.exception.advice.ErrorCode;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.member.dto.response.LoginResponse;
import com.example.interviewPrep.quiz.member.mentee.domain.Mentee;
import com.example.interviewPrep.quiz.member.mentee.repository.MenteeRepository;
import com.example.interviewPrep.quiz.redis.RedisDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;


@Service
@RequiredArgsConstructor
public class OauthService {
    private final GoogleOauth googleOauth;
    private final KakaoOauth kakaoOauth;
    private final NaverOauth naverOauth;
    private final HttpServletResponse response;
    private final JwtService jwtService;
    private final MenteeRepository menteeRepository;
    private final RedisDao redisDao;

    public void request(String socialLoginType) {
        String redirectURL;
        switch (socialLoginType) {
            case "google": {
                redirectURL = googleOauth.getOauthRedirectURL();
            }
            break;
            case "kakao": {
                redirectURL = kakaoOauth.getOauthRedirectURL();
            }
            break;
            case "naver": {
                redirectURL = naverOauth.getOauthRedirectURL();
            }
            break;
            default: {
                throw new CommonException(ErrorCode.NOT_FOUND_SOCIAL_TYPE);
            }
        }
        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LoginResponse socialLogin(String socialLoginType, String code) {
        switch (socialLoginType) {
            case "google": {
                return socialLogin(googleOauth.getSocialMember(code));
            }
            case "kakao": {
                return socialLogin(kakaoOauth.getSocialMember(code));
            }
            case "naver": {
                return socialLogin(naverOauth.getSocialMember(code));
            }
            default: {
                throw new CommonException(ErrorCode.NOT_FOUND_SOCIAL_TYPE);
            }
        }
    }

    @Transactional
    public LoginResponse socialLogin(Mentee mentee) {

        Mentee findMentee = menteeRepository.findByEmailAndType(mentee.getEmail(), mentee.getType()).orElse(null);

        if(findMentee == null){
            findMentee = menteeRepository.save(mentee);
        }

        Long menteeId = findMentee.getId();

        String accessToken = jwtService.createAccessToken(menteeId, "mentee");
        String refreshToken = jwtService.createRefreshToken(menteeId, "mentee");

        // 토큰으로부터 유저 정보를 받아옵니다.
        Authentication authentication = jwtService.getAuthentication(String.valueOf(menteeId));
        // SecurityContext 에 Authentication 객체를 저장합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        redisDao.setValues(String.valueOf(menteeId), refreshToken, Duration.ofMinutes(3));

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
        response.addCookie(cookie);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken("httpOnly")
                .success(true)
                .build();
    }

}
