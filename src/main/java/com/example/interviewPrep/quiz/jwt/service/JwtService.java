package com.example.interviewPrep.quiz.jwt.service;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.member.dto.Role;
import com.example.interviewPrep.quiz.member.service.CustomUserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.INVALID_TOKEN;
import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_ID;

@Service
public class JwtService {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    // access 토큰 유효 시간 30m
    private final long accessTokenValidTime = Duration.ofMinutes(30).toMillis();
    // 리프레시 토큰 유효 시간 1주
    private final long refreshTokenValidTime = Duration.ofDays(7).toMillis();
    private final Key key;

    public JwtService(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createAccessToken(Long memberId, Role role){
        Date now = new Date();
        return Jwts.builder()
                .setId(Long.toString(memberId))
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("admin")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .claim("id", Long.toString(memberId))
                .claim("role", role)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long memberId, Role role){
        Date now = new Date();
        return Jwts.builder()
                .setId(Long.toString(memberId))
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("admin")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .claim("id", Long.toString(memberId))
                .claim("role", role)
                .signWith(key)
                .compact();
    }


    public Claims decode(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getMemberId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String memberId = authentication.getName();
            return Long.parseLong(memberId);
        } catch (Exception e) {
            throw new CommonException(NOT_FOUND_ID);
        }
    }

    public Long getExpirations(String token) {
        // accessToken 남은 유효시간
        Date expiration;
        try {
            expiration = decode(token).getExpiration();
        } catch (Exception e) {
            throw new CommonException(INVALID_TOKEN);
        }
        // 현재 시간
        long now = new Date().getTime();
        return (expiration.getTime() - now);
    }


    public Authentication getAuthentication(String memberId) {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(memberId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


}
