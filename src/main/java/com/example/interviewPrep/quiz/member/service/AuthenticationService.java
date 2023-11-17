package com.example.interviewPrep.quiz.member.service;

import com.example.interviewPrep.quiz.emitter.repository.EmitterService;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.exception.advice.ErrorCode;
import com.example.interviewPrep.quiz.exception.advice.LoginException;
import com.example.interviewPrep.quiz.member.mentee.domain.Mentee;
import com.example.interviewPrep.quiz.member.dto.request.LoginRequest;
import com.example.interviewPrep.quiz.member.dto.response.LoginResponse;
import com.example.interviewPrep.quiz.member.mentee.repository.MenteeRepository;
import com.example.interviewPrep.quiz.member.mentor.domain.Mentor;
import com.example.interviewPrep.quiz.member.mentor.repository.MentorRepository;
import com.example.interviewPrep.quiz.redis.RedisService;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_LOGIN;
import static com.example.interviewPrep.quiz.member.dto.response.LoginResponse.createLoginResponse;

@Slf4j
@Service
@Transactional(readOnly=true)
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final MenteeRepository menteeRepository;

    private final MentorRepository mentorRepository;
    private final EmitterService emitterService;

    private final RedisService redisService;

    public AuthenticationService(JwtUtil jwtUtil, MenteeRepository menteeRepository, MentorRepository mentorRepository, EmitterService emitterService, RedisService redisService) {
        this.jwtUtil = jwtUtil;
        this.menteeRepository = menteeRepository;
        this.mentorRepository = mentorRepository;
        this.emitterService = emitterService;
        this.redisService = redisService;
    }

    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response) {

        String type = loginRequest.getType();

        if(type.equals("Mentee")){
            return menteeLogin(loginRequest, response);
        }

        return mentorLogin(loginRequest, response);
    }


    public LoginResponse menteeLogin(LoginRequest loginRequest, HttpServletResponse response){

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Mentee mentee = menteeRepository.findByEmail(email)
                                        .orElseThrow(() -> new CommonException(NOT_FOUND_LOGIN));

        String savedPassword = mentee.getPassword();

        if (!isMatchPassword(savedPassword, password)) {
            throw new CommonException(NOT_FOUND_LOGIN);
        }

        List<String> tokens = makeNewTokens(mentee.getId(), "Mentee");

        String accessToken = tokens.get(0);
        String refreshToken = tokens.get(1);

        String menteeIdWithPrefix = "Mentee" + Long.toString(mentee.getId());

        setNewAuthentication(menteeIdWithPrefix);

        redisService.setRefreshTokenOnRedis(menteeIdWithPrefix, refreshToken);

        setNewCookie(response, refreshToken);

        return createLoginResponse(accessToken, "httpOnly");
    }


    public LoginResponse mentorLogin(LoginRequest loginRequest, HttpServletResponse response){

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Mentor mentor = mentorRepository.findByEmail(email)
                .orElseThrow(() -> new CommonException(NOT_FOUND_LOGIN));

        String savedPassword = mentor.getPassword();

        if (!isMatchPassword(savedPassword, password)) {
            throw new CommonException(NOT_FOUND_LOGIN);
        }

        List<String> tokens = makeNewTokens(mentor.getId(), "Mentor");

        String accessToken = tokens.get(0);
        String refreshToken = tokens.get(1);

        String mentorIdWithPrefix = "Mentor" + mentor.getId();

        setNewAuthentication(mentorIdWithPrefix);

        redisService.setRefreshTokenOnRedis(mentorIdWithPrefix, refreshToken);

        setNewCookie(response, refreshToken);

        return createLoginResponse(accessToken, "httpOnly");


    }


    public boolean isMatchPassword(String savedPassword, String password) {
        String encryptedPassword = SHA256Util.encryptSHA256(password);
        return savedPassword.equals(encryptedPassword);
    }

    public List<String> makeNewTokens(Long memberId, String type) {
        String accessToken = jwtUtil.createAccessToken(memberId, type);
        String refreshToken = jwtUtil.createRefreshToken(memberId, type);
        return List.of(accessToken, refreshToken);
    }

    public void setNewAuthentication(String memberIdWithPrefix) {
        Authentication authentication = jwtUtil.getAuthentication(memberIdWithPrefix);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void setNewCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
        response.addCookie(cookie);
    }

    public LoginResponse reissue(String refreshToken) {
        if (refreshToken.equals("0")) throw new LoginException(ErrorCode.INVALID_TOKEN);

        String memberId = getMemberId(refreshToken);
        String type = getMemberType(refreshToken);
        String memberIdWithPrefix;

        if(type.equals("Mentee")){
            memberIdWithPrefix = "Mentee" + memberId;
        }else{
            memberIdWithPrefix = "Mentor" + memberId;
        }

        if (redisService.isNotValidRefreshToken(refreshToken, memberIdWithPrefix)) {
            throw new LoginException(ErrorCode.INVALID_TOKEN);
        }

        String accessToken = jwtUtil.createAccessToken(Long.valueOf(memberId), type);

        return createLoginResponse(accessToken, "httpOnly");
    }

    public String getMemberId(String token) {
        Claims claims = jwtUtil.decode(token);
        return claims.get("id", String.class);
    }


    public String getMemberType(String token) {
        Claims claims = jwtUtil.decode(token);
        return claims.get("type", String.class);
    }

    public void logout(String token) {
        String accessToken = token.substring(7);
        Long expiration = jwtUtil.getExpirations(accessToken);
        String memberId = JwtUtil.getMemberId().toString();

        redisService.deleteMemberOnRedis(memberId);

        redisService.setLogOutWithAccessTokenOnRedis(accessToken, expiration);

        emitterService.deleteMemberEmitter(memberId);
    }


}
