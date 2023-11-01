package com.example.interviewPrep.quiz.question.controller;

import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.question.dto.FilterDTO;
import com.example.interviewPrep.quiz.question.dto.QuestionRequest;
import com.example.interviewPrep.quiz.question.dto.QuestionResponse;
import com.example.interviewPrep.quiz.question.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@CrossOrigin(origins = "*")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody QuestionRequest questionRequest) {
        questionService.createQuestion(questionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestion(id));
    }


    @GetMapping("/all")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @GetMapping("/by-type/{type}")
    public ResponseEntity<Page<QuestionResponse>> getQuestionsByType(@PathVariable(required = false) String type, @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(questionService.findByType(type, pageable));
    }


    @GetMapping("/by-company/{companyName}")
    public ResponseEntity<List<QuestionResponse>> getQuestionsByCompany(@PathVariable String companyName) {
        return ResponseEntity.ok(questionService.findByCompany(companyName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> update(@PathVariable Long id, @RequestBody @Valid QuestionRequest questionRequest) {
        return ResponseEntity.ok(questionService.updateQuestion(id, questionRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/total")
    public ResponseEntity<Integer> getTotalQuestionsCount() {
        return ResponseEntity.ok(questionService.getTotalQuestionsCount());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<FilterDTO>> getFilterLanguage() {
        return ResponseEntity.ok(questionService.findFilterLanguage());
    }


}