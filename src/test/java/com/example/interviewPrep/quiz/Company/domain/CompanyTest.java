package com.example.interviewPrep.quiz.Company.domain;

import com.example.interviewPrep.quiz.company.domain.Company;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CompanyTest {

    @Test
    void createCompany(){
        Company company = Company.builder()
                .name("google")
                .build();

        assertThat(company.getName()).isEqualTo("google");
    }


}
