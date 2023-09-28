package com.example.interviewPrep.quiz.product.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductRequest {

    private int type;

    private Long memberLevel;

    private Long mentorId;

    private LocalDateTime interviewDateTime;

}
