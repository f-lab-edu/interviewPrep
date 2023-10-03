package com.example.interviewPrep.quiz.answer.controller;

import com.example.interviewPrep.quiz.answer.dto.request.AnswerRequest;
import com.example.interviewPrep.quiz.answer.dto.response.AnswerResponse;
import com.example.interviewPrep.quiz.answer.dto.response.SolutionResponse;
import com.example.interviewPrep.quiz.answer.service.AnswerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/answers")
@CrossOrigin(origins = "*")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService){
        this.answerService = answerService;
    }

    @PostMapping()
    public ResponseEntity<Void> createAnswer(@RequestBody AnswerRequest answerRequest){
        answerService.createAnswer(answerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<AnswerResponse> readAnswer(@PathVariable Long id){
        return ResponseEntity.ok(answerService.readAnswer(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnswerResponse> updateAnswer(@PathVariable Long id, @RequestBody AnswerRequest answerRequest){
        return ResponseEntity.ok(answerService.updateAnswer(id, answerRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id){
        answerService.deleteAnswer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/solution/{id}/by-type/{type}")
    public ResponseEntity<Page<SolutionResponse>> findSolutionAnswer(@PathVariable Long id, @PathVariable String type,
                                                                     @PageableDefault(size=10) Pageable pageable){
        return ResponseEntity.ok(answerService.getSolution(id, type, pageable));
    }


    @GetMapping("/solution/check/{id}")
    public ResponseEntity<Void> checkMySolution(@PathVariable Long id){
        answerService.checkMySolution(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}