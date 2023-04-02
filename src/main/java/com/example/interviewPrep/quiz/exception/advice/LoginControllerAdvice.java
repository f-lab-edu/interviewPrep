package com.example.interviewPrep.quiz.exception.advice;

import com.example.interviewPrep.quiz.response.ErrorResponse;
import com.example.interviewPrep.quiz.response.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice("com.example.interviewPrep.quiz")
public class LoginControllerAdvice {

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResultResponse<?> loginExHandler(LoginException ex) {
        //log.error("[exceptionHandler] ex:", ex);
        return ResultResponse.fail(ErrorResponse.of(ex.getError()));
    }

}
