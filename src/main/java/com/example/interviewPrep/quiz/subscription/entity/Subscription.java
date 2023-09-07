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
@NoArgsConstructor
public class Subscription extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUBSCRIPTION_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private LocalDate startDate;

    private LocalDate endDate;

    private int totalFee;

    private boolean isValid;

    @Builder
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
        this.isValid = isValid;
    }

    public static Subscription createSubscriptionEntity(Member member, LocalDate startDate, LocalDate endDate, int totalFee, boolean isValid) {
        return Subscription.builder()
                .member(member)
                .startDate(startDate)
                .endDate(endDate)
                .totalFee(totalFee)
                .isValid(isValid)
                .build();
    }

    public void cancel(boolean isValid) {
        this.isValid = isValid;
    }
}
