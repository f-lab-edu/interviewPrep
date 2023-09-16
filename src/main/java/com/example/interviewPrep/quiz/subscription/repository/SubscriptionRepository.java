package com.example.interviewPrep.quiz.subscription.repository;

import com.example.interviewPrep.quiz.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription save(Subscription subscription);

    @Query("SELECT s FROM Subscription s WHERE s.member.id=?1 AND s.isValid=?2")
    Optional<Subscription> findByMemberId(Long memberId, boolean isValid);
}
