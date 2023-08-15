package com.example.interviewPrep.quiz.question.controller;

import com.example.interviewPrep.quiz.question.dto.QuestionRequest;
import com.example.interviewPrep.quiz.response.ResultResponse;
import com.example.interviewPrep.quiz.question.service.QuestionService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService){
        this.questionService = questionService;
    }

    @PostMapping
    public ResultResponse<?> create(@RequestBody @Valid QuestionRequest questionRequest){
        return ResultResponse.success(questionService.createQuestion(questionRequest));
    }

    @GetMapping("/api/v1/question/single/{id}")
    public ResultResponse<?> getQuestion(@PathVariable Long id){
        return ResultResponse.success(questionService.getQuestion(id));
    }

    @GetMapping({"/api/v1/question/{type}", ""})
    public ResultResponse<?> getQuestionsByType(@PathVariable(required = false) String type, @PageableDefault(size=10) Pageable pageable){
        return ResultResponse.success(questionService.findByType(type, pageable));
    }

    @PutMapping("/api/v1/question/{id}")
    public ResultResponse<?> update(@PathVariable Long id, @RequestBody @Valid QuestionRequest questionRequest){
        return ResultResponse.success(questionService.updateQuestion(id, questionRequest));
    }

    @DeleteMapping("/api/v1/question/{id}")
    public ResultResponse<?> delete(@PathVariable Long id){
        return ResultResponse.success(questionService.deleteQuestion(id));
    }

    @GetMapping("/api/v1/question/all")
    public ResultResponse<?> getTotalQuestionsCount(){
        return ResultResponse.success(questionService.getTotalQuestionsCount());
    }

    @GetMapping("/api/v1/question/filter")
    public ResultResponse<?> getFilterLanguage(){
        return ResultResponse.success(questionService.findFilterLanguage());
    }


}