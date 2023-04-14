package com.example.interviewPrep.quiz.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;


@Aspect
@Component
public class TimerAop {

    @Pointcut("@annotation(com.example.interviewPrep.quiz.aop.Timer)")//Timer 어노테이션이 붙은 메서드에만 적용
    private void enableTimer(){}

    @Around("enableTimer()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable{ //메서드 실행시 걸린시간 측정
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed(); //메서드가 실행되는 부분

        stopWatch.stop();
        System.out.println("total time : "+stopWatch.getTotalTimeSeconds());
    }
}