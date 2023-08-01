package com.example.interviewPrep.quiz.member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class RefreshToken {

    @Id
    @Column(nullable = false)
    private Long memberId;

    private String refreshToken;

}