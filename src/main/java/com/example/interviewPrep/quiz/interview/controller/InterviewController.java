package com.example.interviewPrep.quiz.interview.controller;

import com.example.interviewPrep.quiz.interview.dto.request.InterviewRequest;
import com.example.interviewPrep.quiz.interview.service.InterviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/interviews")
@CrossOrigin("*")
public class InterviewController {


    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping()
    public ResponseEntity<Void> createInterview(@RequestBody InterviewRequest interviewRequest) {
        interviewService.createInterview(interviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
