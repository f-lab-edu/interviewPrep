package com.example.interviewPrep.quiz.notification.domain;

import javax.persistence.*;

import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.answer.domain.AnswerComment;

import com.example.interviewPrep.quiz.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import java.util.Objects;


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
    @JsonBackReference(value="receiver-notification")
    private Member receiver;

    private String receiver_member_id;

    @ManyToOne(fetch = FetchType.LAZY)
    private AnswerComment comment;

    private String content;

    private String url;

    private boolean isRead;

    public Notification(){
    }
    @Builder
    public Notification(Member receiver, AnswerComment comment, String content, boolean isRead) {
        Objects.requireNonNull(receiver, "Receiver가 null입니다.");
        Objects.requireNonNull(comment, "Comment가 null입니다.");
        Objects.requireNonNull(content, "content가 null입니다.");

        this.receiver = receiver;
        this.comment = comment;
        this.content = content;
        this.isRead = isRead;
    }

    public void createReceiverMemberId(String receiver_member_id){
        this.receiver_member_id = receiver_member_id;
    }

    public void read() {
        this.isRead = true;
    }
}