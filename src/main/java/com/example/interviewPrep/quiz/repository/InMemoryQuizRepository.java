package com.example.interviewPrep.quiz.repository;

import com.example.interviewPrep.quiz.dto.ProblemDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryQuizRepository implements QuizRepository{

    private static final List<ProblemDTO> problems = new ArrayList<>();

    @Override
    public List<ProblemDTO> init() {
        return problems;
    }
}
