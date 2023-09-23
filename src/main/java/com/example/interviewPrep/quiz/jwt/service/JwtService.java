package com.example.interviewPrep.quiz.jwt.service;

import com.example.interviewPrep.quiz.member.dto.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Service
public class JwtService {


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


}
