package com.example.interviewPrep.quiz.answer.domain;

import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.notification.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ANSWER_COMMENT_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ANSWER_ID")
    Answer answer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    Member member;

    // Notification 엔티티와 @OneToMany 참조 관계를 설정하였습니다
    // 연관관계의 주인을 Notification.comment로 설정하였습니다
    @OneToMany(mappedBy = "comment")
    @JoinColumn(name = "NOTIFICATION_ID")
    List<Notification> notifications;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ANSWER_WRITER_ID")
    Member answerWriter;

    private String comment;

    public void change(String comment){
        this.comment = comment;
    }

}
