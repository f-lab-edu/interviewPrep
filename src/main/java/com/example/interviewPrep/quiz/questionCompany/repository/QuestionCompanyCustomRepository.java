package com.example.interviewPrep.quiz.questionCompany.repository;

import com.example.interviewPrep.quiz.questionCompany.domain.QuestionCompany;

import java.util.List;

public interface QuestionCompanyCustomRepository {

    List<QuestionCompany> findByCompanyId(Long companyId);
}
