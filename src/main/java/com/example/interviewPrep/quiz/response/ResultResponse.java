package com.example.interviewPrep.quiz.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) //null 제외
public class ResultResponse<T> {

    private boolean success;
    private T data;
    private ErrorResponse error;


    private ResultResponse(T data) {
        this.data = data;
    }


    public static <T> ResultResponse<T> success() {
        return new ResultResponse<>(true, null, null);
    }

    public static <T> ResultResponse<T> success(T data) {
        return new ResultResponse<>(true,data,null);
    }

    public static <T> ResultResponse<T> fail(ErrorResponse errorResponse) {
        return new ResultResponse<>(false, null, errorResponse);
    }

}
