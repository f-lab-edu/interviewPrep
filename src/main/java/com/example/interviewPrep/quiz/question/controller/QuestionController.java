package com.example.interviewPrep.quiz.question.controller;

import com.example.interviewPrep.quiz.aop.Timer;
import com.example.interviewPrep.quiz.response.ResultResponse;
import com.example.interviewPrep.quiz.question.dto.QuestionDTO;
import com.example.interviewPrep.quiz.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class QuestionController {

    private final QuestionService questionService;

    @Timer // redis 유무에 따른 api 응답시간을 체크를 위해 시간 측정 aop 사용
    @GetMapping({"/{type}", ""})
    public ResultResponse<?> getQuestionType(@PathVariable(required = false) String type,
                                             @PageableDefault(size=10) Pageable pageable){
        return ResultResponse.success(questionService.findByType(type, pageable));
    }


    @GetMapping("/single/{id}")
    public ResultResponse<?> getQuestion(@PathVariable Long id){
        return ResultResponse.success(questionService.getQuestion(id));
    }


    @PostMapping
    public ResultResponse<?> create(@RequestBody @Valid QuestionDTO questionDTO){
        return ResultResponse.success(questionService.createQuestion(questionDTO));
    }

    @PutMapping("{id}")
    public ResultResponse<?> update(@PathVariable Long id, @RequestBody @Valid QuestionDTO questionDTO){
        return ResultResponse.success(questionService.updateQuestion(id, questionDTO));
    }

    @DeleteMapping("{id}")
    public ResultResponse<?> delete(@PathVariable Long id){
        return ResultResponse.success(questionService.deleteQuestion(id));
    }


    @GetMapping("/filter")
    public ResultResponse<?> getFilterLanguage(){
        return ResultResponse.success(questionService.findFilterLanguage());
    }


}