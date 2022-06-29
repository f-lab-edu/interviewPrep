package com.example.interviewPrep.quiz.controller;


import com.example.interviewPrep.quiz.dto.ProblemDTO;
import com.example.interviewPrep.quiz.service.QuizService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quizes")
@CrossOrigin
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService){
        this.quizService = quizService;
    }

    @GetMapping
    public List<ProblemDTO> list(){
        return quizService.getQuiz();
    }

}
