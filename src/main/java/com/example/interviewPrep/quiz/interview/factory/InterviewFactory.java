package com.example.interviewPrep.quiz.interview.factory;

import com.example.interviewPrep.quiz.interview.domain.Interview;
import com.example.interviewPrep.quiz.interview.repository.InterviewRepository;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.product.domain.Product;
import com.example.interviewPrep.quiz.utils.InterviewCnt;
import com.example.interviewPrep.quiz.utils.ProductType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InterviewFactory {

    private final InterviewRepository interviewRepository;

    public InterviewFactory(InterviewRepository interviewRepository){
        this.interviewRepository = interviewRepository;
    }


    public void createInterviews(Member member, Product product, Long memberLevel, Long mentorId, LocalDateTime dateTime){

        int type = product.getType();
        int interviewCnt = 0;

        if(type == ProductType.ONE.getType()){
            interviewCnt = InterviewCnt.ONE.getCnts();
        }else if(type == ProductType.TWO.getType()){
            interviewCnt = InterviewCnt.THREE.getCnts();
        }else if(type == ProductType.THREE.getType()){
            interviewCnt = InterviewCnt.SIX.getCnts();
        }

        for(int i=0; i<interviewCnt; i++){

            LocalDateTime interviewDateTime = (i == 0) ? dateTime : null;
            Long interviewMentorId = (i == 0) ? mentorId : 0;

            Interview interview = Interview.builder()
                                  .member(member)
                                  .product(product)
                                  .memberLevel(memberLevel)
                                  .mentorId(interviewMentorId)
                                  .interviewDateTime(interviewDateTime)
                                  .build();

            interviewRepository.save(interview);
        }


    }
}
