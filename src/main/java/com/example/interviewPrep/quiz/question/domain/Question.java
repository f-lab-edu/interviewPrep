package com.example.interviewPrep.quiz.question.domain;

import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@Table(indexes = @Index(name = "i_question", columnList = "title"))
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUESTION_ID")
    private Long id;
    private String title;
    private String type;
    private String difficulty;
    private boolean freeOfCharge;

    @Builder
    public Question(Long id, String title, String type, String difficulty, boolean freeOfCharge) {

        Objects.requireNonNull(id, "id가 null입니다.");
        Objects.requireNonNull(title, "title이 null입니다.");
        Objects.requireNonNull(type, "type이 null입니다.");
        Objects.requireNonNull(difficulty, "difficulty가 null입니다.");

        this.id = id;
        this.title = title;
        this.type = type;
        this.difficulty = difficulty;
        this.freeOfCharge = freeOfCharge;
    }

    public void changeTitleOrType(String title, String type) {
        this.title = title;
        this.type = type;
    }
}
