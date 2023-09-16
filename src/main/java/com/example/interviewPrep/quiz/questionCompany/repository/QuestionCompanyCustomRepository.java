package com.example.interviewPrep.quiz.questionCompany.repository;

import com.example.interviewPrep.quiz.question.domain.Question;

import java.util.List;

public interface QuestionCompanyCustomRepository {

    List<Question> findQuestionsByCompanyId(Long companyId);
}
