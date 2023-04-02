package com.example.interviewPrep.quiz.heart.controller;

import com.example.interviewPrep.quiz.heart.service.RefHeartService;
import com.example.interviewPrep.quiz.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ref-heart")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Log4j2
public class RefHeartController {
    private final RefHeartService refHeartService;

    @PostMapping("{id}")
    public ResultResponse<?> create(@PathVariable Long id) {
        return ResultResponse.success(refHeartService.createRefHeart(id));
    }

    @DeleteMapping("{id}")
    public ResultResponse<?> delete(@PathVariable Long id) {
        return ResultResponse.success(refHeartService.deleteRefHeart(id));
    }
}
