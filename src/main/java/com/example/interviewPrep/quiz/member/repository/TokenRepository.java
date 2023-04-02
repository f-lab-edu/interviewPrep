package com.example.interviewPrep.quiz.member.repository;

import com.example.interviewPrep.quiz.member.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsByRefreshToken(String token);
}