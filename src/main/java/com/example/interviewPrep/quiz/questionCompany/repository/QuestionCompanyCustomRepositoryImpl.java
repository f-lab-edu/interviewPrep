package com.example.interviewPrep.quiz.questionCompany.repository;

import com.example.interviewPrep.quiz.questionCompany.domain.QuestionCompany;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.interviewPrep.quiz.questionCompany.domain.QQuestionCompany.questionCompany;

@Repository
public class QuestionCompanyCustomRepositoryImpl implements QuestionCompanyCustomRepository {

    private JPAQueryFactory jpaQueryFactory;

    public QuestionCompanyCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<QuestionCompany> findByCompanyId(Long companyId) {
        return jpaQueryFactory.selectFrom(questionCompany)
                .where(questionCompany.company.id.eq(companyId))
                .fetch();
    }

}
