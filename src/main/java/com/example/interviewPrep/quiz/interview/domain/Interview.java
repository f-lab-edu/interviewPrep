package com.example.interviewPrep.quiz.interview.domain;


import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.List;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor
public class Interview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INTERVIEW_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy="interview")
    private List<Answer> answers;

    public Interview(Long id, Member member, List<Answer> answers){

        Objects.requireNonNull(id, "id가 null입니다.");
        Objects.requireNonNull(member, "member가 null입니다.");
        Objects.requireNonNull(answers, "answers가 null입니다.");

        this.id = id;
        this.member = member;
        this.answers = answers;
    }


}
