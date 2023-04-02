package com.example.interviewPrep.quiz.member.repository;

import com.example.interviewPrep.quiz.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndType(String email, String type);

    Optional<Member> findById(Long id);

    Member save(Member member);

    void delete(Member member);
    boolean existsByNickName(String nickName);
    boolean existsByEmail(String email);
}
