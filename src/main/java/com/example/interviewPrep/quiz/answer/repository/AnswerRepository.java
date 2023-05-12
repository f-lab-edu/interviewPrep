package com.example.interviewPrep.quiz.answer.repository;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.aop.Timer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findById(Long id);

    Answer save(Answer answer);

    Answer saveAndFlush(Answer answer);

    void delete(Answer answer);


    @Query("SELECT a FROM Answer a, Member m " +
            "where a.question.id = ?1 and m.id not in(?2) and a.member.id = m.id ORDER BY a.heartCnt desc")
    Page<Answer> findSolution(Long id, Long memberId, Pageable pageable);

    @Query("SELECT a FROM Answer a, Member m " +
            "where a.question.id = ?1 and m.id in(?2) and a.member.id = m.id ORDER BY a.heartCnt desc")
    Page<Answer> findMySolution(Long id, Long memberId, Pageable pageable);


    @Transactional
    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select s from Answer s where s.id = :id")
    Optional<Answer> findByIdWithOptimisticLock(@Param("id") Long id);


    @Query("select a.question.id from Answer a where a.question.id in ?1 and a.member.id = ?2 ")
    List<Long> findMyAnswer(List<Long> qList, Long memberId); //@Param("memberId")


    List<Answer> findAllByQuestionIdAndMemberId(Long questionId, Long memberId);

}