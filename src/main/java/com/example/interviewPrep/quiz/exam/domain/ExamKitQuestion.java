/*
package com.example.interviewPrep.quiz.exam.domain;

import com.example.interviewPrep.quiz.question.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExamKitQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EXAMKIT_ID")
    private ExamKit examKit;

    @ManyToOne
    @JoinColumn(name = "QUESTION_ID")
    private Question question;
}
*/