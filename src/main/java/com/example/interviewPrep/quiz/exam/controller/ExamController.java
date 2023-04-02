/*
package com.example.interviewPrep.quiz.exam.controller;

import com.example.interviewPrep.quiz.answer.dto.AnswerDTO;
import com.example.interviewPrep.quiz.exam.dto.ExamKitReq;
import com.example.interviewPrep.quiz.exam.service.ExamService;
import com.example.interviewPrep.quiz.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("exam")
@CrossOrigin("*")
public class ExamController {
    private final ExamService examService;

    @GetMapping("kit")
    public ResultResponse<?> findExamAll() {
        return ResultResponse.success(examService.findExamKit());
    }

    @PostMapping("kit")
    public ResultResponse<?> createExamKitById(@RequestBody ExamKitReq dto) {
        return ResultResponse.success(examService.saveExamKitById(dto));
    }

    @GetMapping("kit/{id}")
    public ResultResponse<?> loadExamQuestion(@PathVariable Long id) {
        return ResultResponse.success(examService.loadExamQuestion(id));
    }

    @PostMapping("kit/{id}")
    public ResultResponse<?> saveExam(@PathVariable Long id, @RequestBody List<AnswerDTO> answerList) {
        return ResultResponse.success(examService.saveExam(id, answerList));
    }
    @GetMapping("my")
    public ResultResponse<?> myExam() {
        return ResultResponse.success(examService.findMyExam());
    }

    @GetMapping("my/{id}")
    public ResultResponse<?> readExam(@PathVariable Long id) {
        return ResultResponse.success(examService.readExam(id));
    }

}
*/