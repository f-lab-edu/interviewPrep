package com.example.interviewPrep.quiz.interview.domain;

import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class Interview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member mentor;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    private Long memberLevel;

    private Long mentorId;

    private LocalDateTime interviewDateTime;

    @Builder
    public Interview(Member member, Member mentor, Product product, Long memberLevel, Long mentorId, LocalDateTime interviewDateTime) {

        Objects.requireNonNull(member, "member가 null입니다.");
        Objects.requireNonNull(mentor, "mentor가 null입니다.");
        Objects.requireNonNull(product, "product가 null입니다.");

        this.member = member;
        this.mentor = mentor;
        this.product = product;
        this.memberLevel = memberLevel;
        this.mentorId = mentorId;
        this.interviewDateTime = interviewDateTime;
    }
}
