package com.example.interviewPrep.quiz.subscription.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SubscriptionResponse {

    private final int monthDuration;

    @Builder
    public SubscriptionResponse(int monthDuration) {
        this.monthDuration = monthDuration;
    }

    public static SubscriptionResponse createSubscriptionResponse(int monthDuration) {
        return SubscriptionResponse.builder()
                .monthDuration(monthDuration)
                .build();
    }
}
