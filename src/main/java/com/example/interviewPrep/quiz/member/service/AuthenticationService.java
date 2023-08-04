package com.example.interviewPrep.quiz.member.service;

import com.example.interviewPrep.quiz.emitter.repository.EmitterRepository;
import com.example.interviewPrep.quiz.emitter.repository.EmitterService;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.exception.advice.ErrorCode;
import com.example.interviewPrep.quiz.exception.advice.LoginException;
import com.example.interviewPrep.quiz.member.dto.LoginRequestDTO;
import com.example.interviewPrep.quiz.member.dto.LoginResponseDTO;
import com.example.interviewPrep.quiz.member.dto.Role;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.redis.RedisDao;
import com.example.interviewPrep.quiz.redis.RedisService;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import io.jsonwebtoken.Claims;
import java.util.List;
import javax.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_LOGIN;

@Slf4j
@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final EmitterService emitterService;

    private final RedisService redisService;

    public AuthenticationService(JwtUtil jwtUtil, MemberRepository memberRepository, EmitterService emitterService, RedisService redisService){
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
        this.emitterService = emitterService;
        this.redisService = redisService;
    }

    public LoginResponseDTO login(LoginRequestDTO memberDTO, HttpServletResponse response) {

        String email = memberDTO.getEmail();
        String inputPassword = memberDTO.getPassword();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new CommonException(NOT_FOUND_LOGIN));

        String savedPassword = member.getPassword();

        if(!isMatchPassword(savedPassword, inputPassword)) {
            throw new CommonException(NOT_FOUND_LOGIN);
        }

        List<String> tokens = makeNewTokens(member.getId(), member.getRole());

        String accessToken = tokens.get(0);
        String refreshToken = tokens.get(1);

        setNewAuthentication(member.getId());

        redisService.setRefreshTokenOnRedis(member.getId(), refreshToken);

        setNewCookie(response, refreshToken);
        return LoginResponseDTO.builder()
            .accessToken(accessToken)
            .refreshToken("httpOnly")
            .build();
    }

    public boolean isMatchPassword(String savedPassword, String inputPassword){
        String encryptedInputPassword = SHA256Util.encryptSHA256(inputPassword);
        return savedPassword.equals(encryptedInputPassword);
    }

    public List<String> makeNewTokens(Long memberId, Role role){
        String accessToken = jwtUtil.createAccessToken(memberId, role);
        String refreshToken = jwtUtil.createRefreshToken(memberId, role);
        return List.of(accessToken, refreshToken);
    }

    public void setNewAuthentication(Long memberId){
        Authentication authentication = jwtUtil.getAuthentication(String.valueOf(memberId));
        // SecurityContext 에 Authentication 객체를 저장합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void setNewCookie(HttpServletResponse response, String refreshToken){
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7*24*60*60);
        cookie.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
        response.addCookie(cookie);
    }

    public LoginResponseDTO reissue(String refreshToken){
        if(refreshToken.equals("0")) throw new LoginException(ErrorCode.INVALID_TOKEN);

        String memberId = getMemberId(refreshToken);
        Role role = getRoleType(refreshToken);

        if(redisService.isNotValidRefreshToken(refreshToken, memberId)){
            throw new LoginException(ErrorCode.INVALID_TOKEN);
        }

        String accessToken = jwtUtil.createAccessToken(Long.valueOf(memberId), role);

        return LoginResponseDTO.builder()
               .accessToken(accessToken)
               .refreshToken("httpOnly")
               .build();
    }

    public String getMemberId(String token){
        Claims claims  = jwtUtil.decode(token);
        return claims.get("id", String.class);
    }

    public Role getRoleType(String token){
        Claims claims  = jwtUtil.decode(token);
        String roleStr = claims.get("role", String.class);

        if(roleStr != null && roleStr.equals("USER")){
            return Role.USER;
        }
        return Role.MENTOR;
    }


    public void logout(String token){
        String accessToken = token.substring(7);
        Long expiration = jwtUtil.getExpirations(accessToken);
        String memberId = JwtUtil.getMemberId().toString();

        redisService.deleteMemberOnRedis(memberId);

        redisService.setLogOutWithAccessTokenOnRedis(accessToken, expiration);

        emitterService.deleteMemberEmitter(memberId);
    }


}
