package com.example.interviewPrep.quiz.member.social.service;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.exception.advice.ErrorCode;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.dto.LoginResponseDTO;
import com.example.interviewPrep.quiz.member.dto.Role;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.redis.RedisDao;
import com.example.interviewPrep.quiz.utils.JwtUtil;
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
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final RedisDao redisDao;

    public void request(String socialLoginType) {
        String redirectURL;
        switch (socialLoginType) {
            case "google": {
                redirectURL = googleOauth.getOauthRedirectURL();
            } break;
            case "kakao": {
                redirectURL = kakaoOauth.getOauthRedirectURL();
            } break;
            case "naver": {
                redirectURL = naverOauth.getOauthRedirectURL();
            } break;
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


    public LoginResponseDTO socialLogin(String socialLoginType, String code) {
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



    public LoginResponseDTO socialLogin(Member s_member){
        Member member = memberRepository.findByEmailAndType(s_member.getEmail(), s_member.getType())
                .map(entity -> entity.update(s_member.getName(), s_member.getPicture()))
                .orElse(createPwd(s_member));
        memberRepository.save(member);

        Long memberId = member.getId();
        Role role = member.getRole();

        String accessToken = jwtUtil.createAccessToken(memberId, role);
        String refreshToken = jwtUtil.createRefreshToken(memberId, role);

        // 토큰으로부터 유저 정보를 받아옵니다.
        Authentication authentication = jwtUtil.getAuthentication(String.valueOf(memberId));
        // SecurityContext 에 Authentication 객체를 저장합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        redisDao.setValues(String.valueOf(memberId), refreshToken, Duration.ofMinutes(3));

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7*24*60*60);
        cookie.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
        response.addCookie(cookie);

        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken("httpOnly")
                .build();
    }


    public Member createPwd(Member member){
        String password = new BigInteger(10, new SecureRandom()).toString();
        member.createPwd(SHA256Util.encryptSHA256(password));
        return member;
    }

}
