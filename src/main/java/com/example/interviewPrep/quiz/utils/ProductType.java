package com.example.interviewPrep.quiz.utils;

import lombok.Getter;

@Getter
public enum ProductType {

    ONE(1), TWO(2), THREE(3);

    private final int type;

    private ProductType(int type) {
        this.type = type;
    }

}
