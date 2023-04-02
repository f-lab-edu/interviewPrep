package com.example.interviewPrep.quiz.question.repository;

import com.example.interviewPrep.quiz.question.domain.QuestionReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReferenceRepository extends JpaRepository<QuestionReference, Long> {

    QuestionReference save(QuestionReference reference);

    Optional<QuestionReference> findById(Long id);

    void delete(QuestionReference reference);

    @Query("SELECT r FROM QuestionReference r WHERE r.question.id = ?1 ORDER BY r.modifiedDate")
    Page<QuestionReference> findByRef(Long id, Pageable pageable);
}
