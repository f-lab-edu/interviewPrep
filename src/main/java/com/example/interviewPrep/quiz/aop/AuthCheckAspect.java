package com.example.interviewPrep.quiz.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

@Aspect
@Component
@Log4j2
@SuppressWarnings("unchecked")
public class AuthCheckAspect {

    /**
     * 멤버의 로그인을 체크한다.
     * @param
     * @return
     * @throws Throwable
     */
    @Before("@annotation(com.example.interviewPrep.quiz.aop.MemberLoginCheck)")
    public void memberLoginCheck(JoinPoint jp) throws Throwable {
        log.debug("AOP - Member Login Check Started");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserDetails userDetails = (UserDetails)principal;

        Long memberId = Long.parseLong(userDetails.getUsername());

        if (memberId == null) {
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "NO_LOGIN") {};
        }
    }
}