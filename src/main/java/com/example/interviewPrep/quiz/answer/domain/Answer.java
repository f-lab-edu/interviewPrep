package com.example.interviewPrep.quiz.answer.domain;

import com.example.interviewPrep.quiz.domain.BaseTimeEntity;

// import com.example.interviewPrep.quiz.exam.domain.ExamAnswer;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.heart.exception.HeartExistException;
import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor
public class Answer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ANSWER_ID")
    private Long id;

    @Lob
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "QUESTION_ID")
    Question question;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    Member member;

    private int commentCnt;

    private int heartCnt;

    @Version
    private Long version;

    public Answer(Long id, String content, Question question, Member member, int commentCnt, int heartCnt, Long version){

        Objects.requireNonNull(id, "id가 null입니다.");
        Objects.requireNonNull(question, "question이 null입니다.");
        Objects.requireNonNull(member, "member이 null입니다.");

        this.id = id;
        this.content = content;
        this.question = question;
        this.member = member;
        this.commentCnt = commentCnt;
        this.heartCnt = heartCnt;
        this.version = version;
    }


    public void change(String content){
        this.content = content;
    }

    public int increase() {
        return ++this.heartCnt;
    }

    public int decrease() {
        if (this.heartCnt <= 0) {
            throw new HeartExistException("좋아요 수가 0보다 작아 좋아요 수를 감소시킬수 없습니다.");
        }
        return --this.heartCnt;
    }

    public int commentIncrease() {
        return ++this.commentCnt;
    }

    public int commentDecrease() {
        return --this.commentCnt;
    }

}
