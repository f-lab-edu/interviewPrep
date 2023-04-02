package com.example.interviewPrep.quiz.question.domain;

import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.question.domain.Question;
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
public class QuestionReference extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUESTION_REF_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "QUESTION_ID")
    Question question;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    Member member;

    private String link;

    public void change(String link){
        this.link = link;
    }

}
