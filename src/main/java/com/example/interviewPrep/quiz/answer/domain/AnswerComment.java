package com.example.interviewPrep.quiz.answer.domain;

import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ANSWER_COMMENT_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ANSWER_ID")
    Answer answer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    Member member;

    private String comment;

    public void change(String comment){
        this.comment = comment;
    }

}
