package com.example.interviewPrep.quiz.answer.controller;

import com.example.interviewPrep.quiz.answer.dto.AnswerDTO;
import com.example.interviewPrep.quiz.answer.service.AnswerService;
import com.example.interviewPrep.quiz.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/answer")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping()
    public ResultResponse<?> createAnswer(@RequestBody @Valid AnswerDTO answerDTO){
        return ResultResponse.success(answerService.createAnswer(answerDTO));
    }


    @GetMapping("/{id}")
    public ResultResponse<?> readAnswer(@PathVariable Long id){
        return ResultResponse.success(answerService.readAnswer(id));
    }


    @DeleteMapping("/{id}")
    public ResultResponse<?> deleteAnswer(@PathVariable Long id){
        return ResultResponse.success(answerService.deleteAnswer(id));
    }


    @GetMapping("/solution/{id}/{type}")
    public ResultResponse<?> findSolutionAnswer(@PathVariable Long id, @PathVariable String type,
                                                @PageableDefault(size=10) Pageable pageable){
        return ResultResponse.success(answerService.getSolution(id, type, pageable));
    }


    @GetMapping("/solution/check/{id}")
    public ResultResponse<?> checkMySolution(@PathVariable Long id){
        answerService.checkMySolution(id);
        return ResultResponse.success();
    }

}