package com.example.interviewPrep.quiz.heart.repository;

import com.example.interviewPrep.quiz.heart.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Heart save(Heart heart);

    Optional<Heart> findById(Long id);

    void delete(Heart heart);

    Optional<Heart> findByAnswerIdAndMemberId(Long answerId, Long memberId);

    List<Heart> findByAnswerId(Long id);

    int countHeartByAnswerId(long id);

    @Query("select h.answer.id from Heart h where h.answer.id in ?1 and h.member.id = ?2")
    List<Long> findMyHeart(List<Long> aList, Long memberId);
}
