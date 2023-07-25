package com.example.interviewPrep.quiz.member.service;

import com.example.interviewPrep.quiz.emitter.repository.EmitterRepository;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.exception.advice.ErrorCode;
import com.example.interviewPrep.quiz.exception.advice.LoginException;
import com.example.interviewPrep.quiz.member.dto.LoginRequestDTO;
import com.example.interviewPrep.quiz.member.dto.LoginResponseDTO;
import com.example.interviewPrep.quiz.member.dto.Role;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.redis.RedisDao;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import io.jsonwebtoken.Claims;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import java.time.Duration;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_LOGIN;

@Slf4j
@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final EmitterRepository emitterRepository;
    private final RedisDao redisDao;

    @Autowired
    public AuthenticationService(JwtUtil jwtUtil, MemberRepository memberRepository, EmitterRepository emitterRepository, RedisDao redisDao){
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
        this.emitterRepository = emitterRepository;
        this.redisDao = redisDao;
    }

    public LoginResponseDTO login(LoginRequestDTO memberDTO, HttpServletResponse response) {

        String email = memberDTO.getEmail();
        String password = memberDTO.getPassword();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new CommonException(NOT_FOUND_LOGIN));


        if(!passwordCheck(member.getPassword(), password)) {
            throw new CommonException(NOT_FOUND_LOGIN);
        }

        List<String> tokens = makeNewTokens(member.getId(), member.getRole());

        String accessToken = tokens.get(0);
        String refreshToken = tokens.get(1);

        setNewAuthentication(member.getId());

        setRefreshTokenOnRedis(member.getId(), refreshToken);

        setNewCookie(response, refreshToken);

        return makeLoginResponseDTO(accessToken);
    }

    public boolean passwordCheck(String memberPassword, String inputPassword){
        String encryptedInputPassword = SHA256Util.encryptSHA256(inputPassword);
        return memberPassword.equals(encryptedInputPassword);
    }

    public List<String> makeNewTokens(Long memberId, Role role){
        List<String> tokens = new ArrayList<>();

        String accessToken = jwtUtil.createAccessToken(memberId, role);
        String refreshToken = jwtUtil.createRefreshToken(memberId, role);

        tokens.add(accessToken);
        tokens.add(refreshToken);

        return tokens;
    }

    public void setNewAuthentication(Long memberId){
        Authentication authentication = jwtUtil.getAuthentication(String.valueOf(memberId));
        // SecurityContext 에 Authentication 객체를 저장합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void setRefreshTokenOnRedis(Long memberId, String refreshToken){
        redisDao.setValues(String.valueOf(memberId), refreshToken, Duration.ofDays(7));
    }

    public void setNewCookie(HttpServletResponse response, String refreshToken){
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7*24*60*60);
        cookie.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
        response.addCookie(cookie);
    }

    public LoginResponseDTO reissue(String token){
        if(token.equals("0")) throw new LoginException(ErrorCode.INVALID_TOKEN);

        String memberId = getMemberId(token);
        Role role = setRoleType(token);

        if(!isValidRefreshToken(token, memberId)){
            throw new LoginException(ErrorCode.INVALID_TOKEN);
        }

        return makeLoginResponseDTO(memberId, role);

    }

    public String getMemberId(String token){
        Claims claims  = jwtUtil.decode(token);
        return claims.get("id", String.class);
    }

    public Role setRoleType(String token){
        Claims claims  = jwtUtil.decode(token);
        String roleStr = claims.get("role", String.class);
        Role role = Role.MENTOR;
        if(roleStr.equals("USER")) role = Role.USER;

        return role;
    }


    public boolean isValidRefreshToken(String token, String memberId){
        String refreshToken = redisDao.getValues(memberId);
        return refreshToken != null && refreshToken.equals(token);
    }


    public LoginResponseDTO makeLoginResponseDTO(String accessToken){
        return LoginResponseDTO.builder()
            .accessToken(accessToken)
            .refreshToken("httpOnly")
            .build();
    }

    public LoginResponseDTO makeLoginResponseDTO(String memberId, Role role){
        String accessToken = jwtUtil.createAccessToken(Long.valueOf(memberId), role);
        return LoginResponseDTO.builder()
            .accessToken(accessToken)
            .refreshToken("httpOnly")
            .build();
    }

    public void logout(String token){
        String accessToken = token.substring(7);
        Long expiration = jwtUtil.getExpirations(accessToken);
        String memberId = JwtUtil.getMemberId().toString();

        deleteMemberOnRedis(memberId);

        setLogOutWithAccessTokenOnRedis(accessToken, expiration);

        deleteMemberEmitter(memberId);
    }

    public void deleteMemberOnRedis(String memberId){
        if(redisDao.getValues(memberId) !=null){
            redisDao.deleteValues(memberId);
        }
    }

    public void setLogOutWithAccessTokenOnRedis(String accessToken, Long expiration){
        redisDao.setValues(accessToken, "logout", Duration.ofMillis(expiration));
    }

    public void deleteMemberEmitter(String memberId){
        emitterRepository.deleteById(memberId);
    }


}
