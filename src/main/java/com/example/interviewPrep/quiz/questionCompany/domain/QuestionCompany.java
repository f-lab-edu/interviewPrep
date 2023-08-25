package com.example.interviewPrep.quiz.questionCompany.domain;


import com.example.interviewPrep.quiz.company.domain.Company;
import com.example.interviewPrep.quiz.question.domain.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
public class QuestionCompany {

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="QUESTION_ID")
    private Question question;

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="COMPANY_ID")
    private Company company;


    public QuestionCompany(Question question, Company company){

        Objects.requireNonNull(question, "question이 null입니다.");
        Objects.requireNonNull(company, "company가 null입니다.");

        this.question = question;
        this.company = company;
    }

}
