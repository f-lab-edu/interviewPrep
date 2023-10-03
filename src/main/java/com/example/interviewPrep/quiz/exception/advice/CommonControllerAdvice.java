package com.example.interviewPrep.quiz.exception.advice;

import com.example.interviewPrep.quiz.response.ErrorResponse;
import com.example.interviewPrep.quiz.response.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;

@Slf4j
@RestControllerAdvice("com.example.interviewPrep.quiz")
public class CommonControllerAdvice {

    @ExceptionHandler()
    public ResponseEntity<ResultResponse<?>> commonExHandler(CommonException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResultResponse.fail(ErrorResponse.of(ex.getError())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultResponse<?> processValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }

        return ResultResponse.fail(ErrorResponse.of(MISSING_PARAMETER.setMissingParameterMsg(builder.toString())));
    }

}
