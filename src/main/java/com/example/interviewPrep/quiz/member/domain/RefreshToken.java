package com.example.interviewPrep.quiz.member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RefreshToken {
    public RefreshToken(){
    }
    public RefreshToken(Long memberId, String refreshToken){
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }

    @Id
    @Column(nullable = false)
    private Long memberId;

    private String refreshToken;

}