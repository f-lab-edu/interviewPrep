package com.example.interviewPrep.quiz.interview.service;


import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.interview.domain.Interview;
import com.example.interviewPrep.quiz.interview.dto.request.InterviewRequest;
import com.example.interviewPrep.quiz.interview.dto.response.InterviewResponse;
import com.example.interviewPrep.quiz.interview.repository.InterviewRepository;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.interviewPrep.quiz.utils.DateFormat.customLocalDateTime;

@Service
public class InterviewService {


    private final InterviewRepository interviewRepository;
    private final MemberRepository memberRepository;

    public InterviewService(InterviewRepository interviewRepository, MemberRepository memberRepository){
        this.interviewRepository = interviewRepository;
        this.memberRepository = memberRepository;
    }


    public InterviewResponse createInterview(InterviewRequest interviewRequest) {

        Long memberId = JwtUtil.getMemberId();

        Member member = memberRepository.findById(memberId).orElse(null);

        List<Answer> answers = interviewRequest.getAnswers();

        Interview interview = Interview.builder()
                                       .member(member)
                                       .answers(answers)
                                       .build();

        interviewRepository.save(interview);

        return InterviewResponse.builder()
                .id(interview.getId())
                .createdDate(customLocalDateTime(interview.getCreatedDate()))
                .build();

    }






}
