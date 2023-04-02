package com.example.interviewPrep.quiz.answer.repository;

import com.example.interviewPrep.quiz.answer.domain.AnswerComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<AnswerComment, Long> {

    AnswerComment save(AnswerComment comment);

    Optional<AnswerComment> findById(Long id);

    void delete(AnswerComment comment);

    @Query("SELECT c FROM AnswerComment c WHERE c.answer.id = ?1 ORDER BY c.createdDate")
    Page<AnswerComment> findByComment(Long id, Pageable pageable);
}
