package com.example.interviewPrep.quiz.member.social.service;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.exception.advice.ErrorCode;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.member.dto.response.LoginResponse;
import com.example.interviewPrep.quiz.member.mentee.domain.Mentee;
import com.example.interviewPrep.quiz.member.mentee.repository.MenteeRepository;
import com.example.interviewPrep.quiz.redis.RedisDao;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
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


    public LoginResponse socialLogin(Mentee mentee) {
        Mentee findMentee = menteeRepository.findByEmailAndType(mentee.getEmail(), mentee.getType())
                .map(entity -> entity.update(mentee.getName(), mentee.getPicture()))
                .orElse(createPwd(mentee));

        Long memberId = findMentee.getId();

        String accessToken = jwtService.createAccessToken(memberId, "mentee");
        String refreshToken = jwtService.createRefreshToken(memberId, "mentee");

        // 토큰으로부터 유저 정보를 받아옵니다.
        Authentication authentication = jwtService.getAuthentication(String.valueOf(memberId));
        // SecurityContext 에 Authentication 객체를 저장합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        redisDao.setValues(String.valueOf(memberId), refreshToken, Duration.ofMinutes(3));

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
        response.addCookie(cookie);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken("httpOnly")
                .build();
    }


    public Mentee createPwd(Mentee mentee) {
        String password = new BigInteger(10, new SecureRandom()).toString();
        mentee.createPwd(SHA256Util.encryptSHA256(password));
        return mentee;
    }

}
