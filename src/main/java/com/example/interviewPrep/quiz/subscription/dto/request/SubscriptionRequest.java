package com.example.interviewPrep.quiz.subscription.dto.request;

import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class SubscriptionRequest {

    private final String type;
    private final int monthDuration;

    public SubscriptionRequest(String type, int monthDuration){
        this.type = type;
        this.monthDuration = monthDuration;
    }
}
