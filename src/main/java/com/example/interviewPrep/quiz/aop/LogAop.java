package com.example.interviewPrep.quiz.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
/*
 @Aspect: 공통적으로 적용될 기능
 @Pointcut: 부가기능이 적용될 대상(메소드)를 선정하는 방법을 의미
 @Around: 대상 객체의 메서드 실행 전, 후 시점에 메소드를 실행
 */
@Slf4j
@Component
@Aspect
public class LogAop {

    @Pointcut("@annotation(com.example.interviewPrep.quiz.aop.Timer)")
    public void timerPointCut(){}

    @Around("timerPointCut()")
    public Object logAOP(ProceedingJoinPoint joinPoint) throws Throwable{

        long start = System.currentTimeMillis();
        log.info("START: " + joinPoint.toLongString());

        try{
             return joinPoint.proceed();
        }finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;

            log.info("END: " + joinPoint.toString()+" "+timeMs+"ms");
        }
    }
}
