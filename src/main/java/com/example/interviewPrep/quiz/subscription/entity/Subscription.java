package com.example.interviewPrep.quiz.subscription.entity;

import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor
public class Subscription extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="SUBSCRIPTION_ID")
    Long id;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name = "MEMBER_ID")
    Member member;

    LocalDate startDate;

    LocalDate endDate;

    int totalFee;

    boolean isValid;

    public Subscription(Long id, Member member, LocalDate startDate, LocalDate endDate, int totalFee, boolean isValid) {
        Objects.requireNonNull(id, "id가 null입니다.");
        Objects.requireNonNull(member, "member가 null입니다.");
        Objects.requireNonNull(startDate, "startDate가 null입니다.");
        Objects.requireNonNull(endDate, "endDate가 null입니다.");

        this.id = id;
        this.member = member;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalFee = totalFee;
    }

    public void setIsValid(boolean isValid){
        this.isValid = isValid;
    }
}
