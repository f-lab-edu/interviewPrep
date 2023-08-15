package com.example.interviewPrep.quiz.answer.controller;

import com.example.interviewPrep.quiz.answer.dto.request.AnswerRequest;
import com.example.interviewPrep.quiz.answer.service.AnswerService;
import com.example.interviewPrep.quiz.response.ResultResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService){
        this.answerService = answerService;
    }

    @PostMapping("/api/v1/answer")
    public ResultResponse<?> createAnswer(@RequestBody @Valid AnswerRequest answerRequest){
        return ResultResponse.success(answerService.createAnswer(answerRequest));
    }


    @GetMapping("/api/v1/answer/{id}")
    public ResultResponse<?> readAnswer(@PathVariable Long id){
        return ResultResponse.success(answerService.readAnswer(id));
    }


    @DeleteMapping("/api/v1/answer/{id}")
    public ResultResponse<?> deleteAnswer(@PathVariable Long id){
        return ResultResponse.success(answerService.deleteAnswer(id));
    }


    @GetMapping("/api/v1/answer/solution/{id}/{type}")
    public ResultResponse<?> findSolutionAnswer(@PathVariable Long id, @PathVariable String type,
                                                @PageableDefault(size=10) Pageable pageable){
        return ResultResponse.success(answerService.getSolution(id, type, pageable));
    }


    @GetMapping("/api/v1/answer/solution/check/{id}")
    public ResultResponse<?> checkMySolution(@PathVariable Long id){
        answerService.checkMySolution(id);
        return ResultResponse.success();
    }

}