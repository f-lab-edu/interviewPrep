package com.example.interviewPrep.quiz.member.mentee.repository;

import com.example.interviewPrep.quiz.member.mentee.domain.Mentee;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface MenteeRepository extends JpaRepository<Mentee, Long> {

    Optional<Mentee> findByEmail(String email);
    Optional<Mentee> findByNickName(String nickName);
}