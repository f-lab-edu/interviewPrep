package com.example.interviewPrep.quiz.interview.repository;

import com.example.interviewPrep.quiz.interview.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
}
