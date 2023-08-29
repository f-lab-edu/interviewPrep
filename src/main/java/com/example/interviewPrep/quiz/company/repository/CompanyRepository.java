package com.example.interviewPrep.quiz.company.repository;

import com.example.interviewPrep.quiz.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByName(String company);
}
