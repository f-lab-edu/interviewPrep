package com.example.interviewPrep.quiz.subscription.controller;

import com.example.interviewPrep.quiz.subscription.dto.request.SubscriptionRequest;
import com.example.interviewPrep.quiz.subscription.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions")
@CrossOrigin(origins = "*")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody SubscriptionRequest subscriptionRequest) {
        subscriptionService.createSubscription(subscriptionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PutMapping()
    public ResponseEntity<Void> stop() {
        subscriptionService.stopSubscription();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
