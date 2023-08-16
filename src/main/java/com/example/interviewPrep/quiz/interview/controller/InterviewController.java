package com.example.interviewPrep.quiz.interview.controller;

import com.example.interviewPrep.quiz.interview.dto.request.InterviewRequest;
import com.example.interviewPrep.quiz.interview.service.InterviewService;
import com.example.interviewPrep.quiz.response.ResultResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@CrossOrigin("*")
public class InterviewController {


    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService){
        this.interviewService = interviewService;
    }


    @PostMapping("/api/v1/interview")
    public ResultResponse<?> createInterview(@RequestBody @Valid InterviewRequest interviewRequest){
        return ResultResponse.success(interviewService.createInterview(interviewRequest));
    }







}
