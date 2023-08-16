package com.example.interviewPrep.quiz.subscription.controller;

import com.example.interviewPrep.quiz.subscription.dto.request.SubscriptionRequest;
import com.example.interviewPrep.quiz.subscription.service.SubscriptionService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService){
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/api/v1/subscription")
    public void create(@RequestBody @Valid SubscriptionRequest subscriptionRequest){
        subscriptionService.createSubscription(subscriptionRequest);
    }


    @PutMapping("/api/v1/subscription")
    public void stop(@RequestBody @Valid SubscriptionRequest subscriptionRequest){
        subscriptionService.stopSubscription(subscriptionRequest);
    }



}
