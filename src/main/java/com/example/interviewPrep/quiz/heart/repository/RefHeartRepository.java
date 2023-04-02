package com.example.interviewPrep.quiz.heart.repository;

import com.example.interviewPrep.quiz.heart.domain.RefHeart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefHeartRepository extends JpaRepository<RefHeart, Long> {
    Optional<RefHeart> findByReferenceIdAndMemberId(Long reference_id, Long member_id);

    int countRefHeartByReferenceId(Long reference_id);
}
