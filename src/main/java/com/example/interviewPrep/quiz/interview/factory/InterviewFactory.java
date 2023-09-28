package com.example.interviewPrep.quiz.interview.factory;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.interview.domain.Interview;
import com.example.interviewPrep.quiz.interview.repository.InterviewRepository;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.product.domain.Product;
import com.example.interviewPrep.quiz.utils.InterviewCnt;
import com.example.interviewPrep.quiz.utils.ProductType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_MEMBER;

@Component
public class InterviewFactory {

    private final MemberRepository memberRepository;
    private final InterviewRepository interviewRepository;

    public InterviewFactory(MemberRepository memberRepository, InterviewRepository interviewRepository){
        this.memberRepository = memberRepository;
        this.interviewRepository = interviewRepository;
    }


    public void createInterviews(Member member, Product product, Long memberLevel, Long mentorId, LocalDateTime dateTime){

        int type = product.getType();
        int interviewCnt = 0;

        Member mentor = memberRepository.findById(mentorId)
                                        .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));

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
                                  .mentor(mentor)
                                  .product(product)
                                  .memberLevel(memberLevel)
                                  .mentorId(interviewMentorId)
                                  .interviewDateTime(interviewDateTime)
                                  .build();

            interviewRepository.save(interview);
        }


    }
}
