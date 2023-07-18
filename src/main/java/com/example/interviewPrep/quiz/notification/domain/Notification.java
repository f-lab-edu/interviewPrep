package com.example.interviewPrep.quiz.notification.domain;

import javax.persistence.*;

import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.answer.domain.AnswerComment;

import com.example.interviewPrep.quiz.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;


@Entity
@Getter
public class Notification extends BaseTimeEntity {

    // Notification의 ID를 나타낸다
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="NOTIFICATION_ID")
    private Long id;

    // Notification을 받아야 하는 MEMBER_ID를 나타낸다
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    @JsonBackReference
    private Member receiver;

    // AnswerComment 엔티티와
    // @ManyToOne 연관관계를 설정하였습니다
    // 순환 참조 방지를 위하여 @JsonBackReference를 추가하였습니다
    @ManyToOne
    @JsonBackReference
    private AnswerComment comment;

    private String content;

    private String url;

    private boolean isRead;

    public Notification(){
    }
    public Notification(Member receiver, AnswerComment comment, String content, boolean isRead) {
        this.receiver = receiver;
        this.comment = comment;
        this.content = content;
        this.isRead = isRead;
    }


    public void read() {
        this.isRead = true;
    }
}