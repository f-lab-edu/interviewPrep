package com.example.interviewPrep.quiz.subscription.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SubscriptionRequest {

    private final String type;
    private final int monthDuration;

    @Builder
    public SubscriptionRequest(String type, int monthDuration) {
        this.type = type;
        this.monthDuration = monthDuration;
    }
}
