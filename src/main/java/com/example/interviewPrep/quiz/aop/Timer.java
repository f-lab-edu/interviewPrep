package com.example.interviewPrep.quiz.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/*
    @Target의 기능은 어노테이션을 붙일 수 있는 대상을 지정하는 것이다.
     METHOD: 메소드
     TYPE: 클래스,인터페이스,열거타입에 어노테이션을 붙일 수 있다는 의미이다.

     @Retention  Annotation 이 실제로 적용되고 유지되는 범위를 의미한다.
     RUNTIME: 컴파일 이후에도 JVM에 의해서 계속 참조가 가능하다. 주로 리플렉션이나 로깅에 많이 사용한다.

 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Timer {
}
