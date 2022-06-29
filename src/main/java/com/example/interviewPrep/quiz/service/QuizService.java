package com.example.interviewPrep.quiz.service;

import com.example.interviewPrep.quiz.dto.ProblemDTO;
import com.example.interviewPrep.quiz.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository){
        this.quizRepository = quizRepository;
    }

    public List<ProblemDTO> getQuiz(){
        return quizRepository.init();
    }

}
