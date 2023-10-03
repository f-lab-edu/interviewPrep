package com.example.interviewPrep.quiz.member.mentor.repository;


import com.example.interviewPrep.quiz.member.mentor.domain.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {

    Optional<Mentor> findByEmail(String email);
    Optional<Mentor> findByNickName(String nickName);
}