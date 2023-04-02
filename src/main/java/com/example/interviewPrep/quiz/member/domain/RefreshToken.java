package com.example.interviewPrep.quiz.member.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    @Column(nullable = false)
    private Long memberId;

    private String refreshToken;
}