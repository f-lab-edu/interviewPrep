package com.example.interviewPrep.quiz.notification.domain;

import javax.persistence.*;

import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.answer.domain.AnswerComment;

import com.example.interviewPrep.quiz.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Notification extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="NOTIFICATION_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_notification_to_receiver"))
    private Member receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_notification_to_review"))
    private AnswerComment comment;

    private String content;

    private String url;

    private boolean isRead;

    @Builder
    public Notification(Member receiver, AnswerComment comment, String content, String url, boolean isRead) {
        this.receiver = receiver;
        this.comment = comment;
        this.content = content;
        this.url = url;
        this.isRead = isRead;
    }

    public void read() {
        this.isRead = true;
    }
}