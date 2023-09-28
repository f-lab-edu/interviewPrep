package com.example.interviewPrep.quiz.interview.service;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.interview.factory.InterviewFactory;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.product.domain.Product;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_MEMBER;

@Service
public class InterviewService {

    private final InterviewFactory interviewFactory;
    private final MemberRepository memberRepository;

    public InterviewService(InterviewFactory interviewFactory, MemberRepository memberRepository) {
        this.interviewFactory = interviewFactory;
        this.memberRepository = memberRepository;
    }


    public void createInterviews(Product product, LocalDateTime interviewDateTime) {

        Long memberId = JwtUtil.getMemberId();

        Member member = memberRepository.findById(memberId)
                                        .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));;

        interviewFactory.createInterviews(member, product, interviewDateTime);
    }


}
