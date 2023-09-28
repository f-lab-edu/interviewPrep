package com.example.interviewPrep.quiz.interview.controller;

import com.example.interviewPrep.quiz.interview.service.InterviewService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/interviews")
public class InterviewController {


    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }



}
