package com.example.interviewPrep.quiz.company.domain;


import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Company extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPANY_ID")
    private Long id;
    private String name;

    @Builder
    public Company(String name) {
        Objects.requireNonNull(name, "name이 null입니다.");
        this.name = name;
    }


}
