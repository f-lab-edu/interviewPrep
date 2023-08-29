package com.example.interviewPrep.quiz.heart.domain;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class Heart extends BaseTimeEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ANSWER_ID")
    Answer answer;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    Member member;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder
    public Heart(Long id, Answer answer, Member member) {
        Objects.requireNonNull(answer, "answer가 null입니다.");
        Objects.requireNonNull(member, "member가 null입니다.");

        this.id = id;
        this.answer = answer;
        this.member = member;
    }
}
