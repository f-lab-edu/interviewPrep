package com.example.interviewPrep.quiz.heart.repository;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnswerLockRepository extends JpaRepository<Answer, Long> {

    @Query(value = "select get_lock(:key, 2)", nativeQuery = true)
    void getLock(@Param("key") String key);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    void releaseLock(@Param("key") String key);
}
