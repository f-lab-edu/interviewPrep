package com.example.interviewPrep.quiz.MemberInterview.domain;

import com.example.interviewPrep.quiz.interview.domain.Interview;
import com.example.interviewPrep.quiz.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
public class MemberInterview {

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="INTERVIEW_ID")
    private Interview interview;


    public MemberInterview(Member member, Interview interview){

        Objects.requireNonNull(member, "member이 null입니다.");
        Objects.requireNonNull(interview, "interview가 null입니다.");

        this.member = member;
        this.interview = interview;
    }

}