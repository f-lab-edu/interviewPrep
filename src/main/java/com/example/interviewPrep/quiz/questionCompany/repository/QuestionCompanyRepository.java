package com.example.interviewPrep.quiz.questionCompany.repository;

import com.example.interviewPrep.quiz.questionCompany.domain.QuestionCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionCompanyRepository extends JpaRepository<QuestionCompany, Long>, QuestionCompanyCustomRepository {
    
}
