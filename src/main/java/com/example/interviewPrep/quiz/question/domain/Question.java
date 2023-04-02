package com.example.interviewPrep.quiz.question.domain;

import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
// import com.example.interviewPrep.quiz.exam.domain.ExamKitQuestion;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name= "i_question", columnList = "title"))
public class Question extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUESTION_ID", insertable=false, updatable=false)
    private Long id;
    private String title;
    private String type;

    // @OneToMany(mappedBy = "question")
    // private List<ExamKitQuestion> questions = new ArrayList<>();

    public void change(String title, String type){
        this.title = title;
        this.type = type;
    }
}
