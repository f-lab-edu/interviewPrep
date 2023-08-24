package com.example.interviewPrep.quiz.question.domain;

import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.question.dto.QuestionRequest;
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

    @Builder
    public Question(Long id, String title, String type) {

        Objects.requireNonNull(id, "id가 null입니다.");
        Objects.requireNonNull(title, "title이 null입니다.");
        Objects.requireNonNull(type, "type이 null입니다.");

        this.id = id;
        this.title = title;
        this.type = type;
    }

    public static Question createQuestionByQuestionRequest(QuestionRequest questionRequest) {
        return Question.builder()
                .id(questionRequest.getId())
                .title(questionRequest.getTitle())
                .type(questionRequest.getType())
                .build();
    }

    public void change(String title, String type) {
        this.title = title;
        this.type = type;
    }
}
