package com.example.interviewPrep.quiz.answer.domain;

import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.notification.domain.Notification;
import java.util.List;
import java.util.Objects;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor
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

    @OneToMany(mappedBy = "comment")
    List<Notification> notifications;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ANSWER_WRITER_ID")
    Member answerWriter;

    private String comment;

    public AnswerComment(Long id, Answer answer, Member member, List<Notification> notifications, Member answerWriter, String comment) {

        Objects.requireNonNull(id, "id가 null입니다.");
        Objects.requireNonNull(answer, "answer이 null입니다.");
        Objects.requireNonNull(member, "member이 null입니다.");
        Objects.requireNonNull(answerWriter, "answerWriter가 null입니다.");

        this.id = id;
        this.answer = answer;
        this.member = member;
        this.notifications = notifications;
        this.answerWriter = answerWriter;
        this.comment = comment;
    }

    public void change(String comment){
        this.comment = comment;
    }

}
