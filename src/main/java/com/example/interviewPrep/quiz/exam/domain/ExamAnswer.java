/*
package com.example.interviewPrep.quiz.exam.domain;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class ExamAnswer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EXAM_ID")
    @JsonIgnore
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "ANSWER_ID")
    @JsonIgnore
    private Answer answer;

}
*/