package com.example.interviewPrep.quiz.question.controller;

import com.example.interviewPrep.quiz.question.dto.ReferenceDTO;
import com.example.interviewPrep.quiz.question.service.ReferenceService;
import com.example.interviewPrep.quiz.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReferenceController {

    private final ReferenceService referenceService;

    @GetMapping("/ref/{id}")
    public ResultResponse<?> findQuestionReference(@PathVariable Long id, @PageableDefault(size=10) Pageable pageable){
        return ResultResponse.success(referenceService.findAnswerReference(id, pageable));
    }

    @PostMapping("/ref")
    public ResultResponse<?> createQuestionReference(@RequestBody @Valid ReferenceDTO referenceDTO){
        return ResultResponse.success(referenceService.createReference(referenceDTO));
    }

    @PutMapping("/ref")
    public ResultResponse<?> updateQuestionReference(@RequestBody @Valid ReferenceDTO referenceDTO){
        referenceService.updateReference(referenceDTO);
        return ResultResponse.success();
    }

    @DeleteMapping("/ref/{id}")
    public ResultResponse<?> deleteQuestionReference(@PathVariable Long id){
        referenceService.deleteReference(id);
        return ResultResponse.success();
    }

}