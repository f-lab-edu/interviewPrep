package com.example.interviewPrep.quiz.subscription.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubscriptionResponse {

    private final int monthDuration;

    public SubscriptionResponse(int monthDuration){
        this.monthDuration = monthDuration;
    }
}
