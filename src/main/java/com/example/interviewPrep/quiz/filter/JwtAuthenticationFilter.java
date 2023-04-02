package com.example.interviewPrep.quiz.filter;

import com.example.interviewPrep.quiz.redis.RedisDao;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisDao redisDao;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer")) {
            try {

                String accessToken = header.substring(7);

                Claims claims = jwtUtil.decode(accessToken);

                boolean valid = !claims.getExpiration().before(new Date());
                if (valid) {
                    if (redisDao.getValues(accessToken) == null) {
                        // 토큰으로부터 유저 정보를 받아옵니다.
                        String memberId = claims.get("id", String.class);
                        Authentication authentication = jwtUtil.getAuthentication(memberId);
                        // SecurityContext 에 Authentication 객체를 저장합니다.
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else request.setAttribute("exception", WRONG_ID_TOKEN);
                }

            } catch (SignatureException e) {
                request.setAttribute("exception", WRONG_TYPE_SIGNATURE);
            } catch (UnsupportedJwtException e) {
                request.setAttribute("exception", WRONG_TOKEN);
            } catch (ExpiredJwtException e) {
                log.error("ExpiredJwtToken JWT token");
                request.setAttribute("exception", EXPIRED_TOKEN);
            } catch (IllegalArgumentException e) {
                request.setAttribute("exception", INVALID_TOKEN);
            }
        }

        filterChain.doFilter(request, response);
    }

}
