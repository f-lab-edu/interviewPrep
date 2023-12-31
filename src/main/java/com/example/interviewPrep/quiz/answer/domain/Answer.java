package com.example.interviewPrep.quiz.answer.domain;

import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.exception.advice.ErrorCode;
import com.example.interviewPrep.quiz.interview.domain.Interview;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.question.domain.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class Answer extends BaseTimeEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "QUESTION_ID")
    Question question;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    Member member;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "INTERVIEW_ID")
    private Interview interview;

    private int commentCnt;

    private int heartCnt;

    @Version
    private Long version;

    @Builder
    public Answer(Long id, String content, Question question, Member member, Interview interview, int commentCnt, int heartCnt, Long version) {
        Objects.requireNonNull(id, "id가 null입니다.");
        Objects.requireNonNull(question, "question이 null입니다.");
        Objects.requireNonNull(member, "member이 null입니다.");

        this.id = id;
        this.content = content;
        this.question = question;
        this.member = member;
        this.interview = interview;
        this.commentCnt = commentCnt;
        this.heartCnt = heartCnt;
        this.version = version;
    }


    public static Answer createAnswerEntity(Member member, Question question, String content) {
        return Answer.builder()
                .member(member)
                .question(question)
                .build();
    }


    public void change(String content) {
        this.content = content;
    }

    public synchronized int increase() {
        return ++this.heartCnt;
    }

    public synchronized int decrease() {
        if (this.heartCnt <= 0) {
            throw new CommonException(ErrorCode.NOT_EXIST_HEART_HISTORY);
        }
        return --this.heartCnt;
    }

    public synchronized int commentIncrease() {
        return ++this.commentCnt;
    }

    public synchronized int commentDecrease() {
        return --this.commentCnt;
    }

}
