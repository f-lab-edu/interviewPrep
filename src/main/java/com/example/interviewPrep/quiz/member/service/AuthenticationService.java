package com.example.interviewPrep.quiz.member.service;

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
import com.example.interviewPrep.quiz.utils.PasswordCheck;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Duration;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_LOGIN;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final RedisDao redisDao;

    public LoginResponseDTO login(LoginRequestDTO memberDTO, HttpServletResponse response) {

        String email = memberDTO.getEmail();
        String password = memberDTO.getPassword();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new CommonException(NOT_FOUND_LOGIN));

        String encryptedPassword = SHA256Util.encryptSHA256(password);
        if(!PasswordCheck.isMatch(member.getPassword(), encryptedPassword)){
            throw new CommonException(NOT_FOUND_LOGIN);
        }

        Long memberId = member.getId();
        Role role = member.getRole();

        String accessToken = jwtUtil.createAccessToken(memberId, role);
        String refreshToken = jwtUtil.createRefreshToken(memberId, role);

        // 토큰으로부터 유저 정보를 받아옵니다.
        Authentication authentication = jwtUtil.getAuthentication(String.valueOf(memberId));

        // SecurityContext 에 Authentication 객체를 저장합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long id = JwtUtil.getMemberId();

        redisDao.setValues(String.valueOf(memberId), refreshToken, Duration.ofDays(7));

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

    public LoginResponseDTO reissue(String token){
        if(token.equals("0")) throw new LoginException(ErrorCode.INVALID_TOKEN);

        Claims claims  = jwtUtil.decode(token);
        String memberId = claims.get("id", String.class);
        String role = claims.get("role", String.class);
        Role myRole = Role.MENTOR;
        if(role.equals("USER")) myRole = Role.USER;

        String refreshToken = redisDao.getValues(memberId);
        if(refreshToken==null || !refreshToken.equals(token))throw new LoginException(ErrorCode.INVALID_TOKEN);

        String accessToken = jwtUtil.createAccessToken(Long.valueOf(memberId), myRole);
        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken("httpOnly")
                .build();
    }


    public void logout(String token, HttpServletResponse response){
        String accessToken = token.substring(7);
        Long expiration = jwtUtil.getExpirations(accessToken);
        String memberId = JwtUtil.getMemberId().toString();

        if(redisDao.getValues(memberId) !=null){
            redisDao.deleteValues(memberId);
        }

        redisDao.setValues(accessToken, "logout", Duration.ofMillis(expiration));

        try {
            response.sendRedirect("/server/logout");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
