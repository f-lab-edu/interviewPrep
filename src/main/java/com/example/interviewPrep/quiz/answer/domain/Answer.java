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

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Answer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ANSWER_ID")
    private Long id;
    @Lob
    private String content;

    @Lob
    private String question_title;

    @ManyToOne(fetch = LAZY)
    @JsonBackReference
    @JoinColumn(name = "QUESTION_ID")
    Question question;

    @ManyToOne(fetch = LAZY)
    @JsonBackReference
    @JoinColumn(name = "MEMBER_ID")
    Member member;

    private int commentCnt;

    private int heartCnt;
    @Version
    private Long version;

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

    public int commentIncrease() { return ++this.commentCnt; }

    public int commentDecrease() {return --this.commentCnt;}

    // @OneToMany(mappedBy = "answer")
    // List<ExamAnswer> exams = new ArrayList<>();
}
