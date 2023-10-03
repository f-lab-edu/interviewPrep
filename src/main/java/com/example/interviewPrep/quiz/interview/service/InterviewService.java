package com.example.interviewPrep.quiz.interview.service;


import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.interview.domain.Interview;
import com.example.interviewPrep.quiz.interview.dto.request.InterviewRequest;
import com.example.interviewPrep.quiz.interview.dto.response.InterviewResponse;
import com.example.interviewPrep.quiz.interview.repository.InterviewRepository;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_MEMBER;
import static com.example.interviewPrep.quiz.interview.domain.Interview.createInterviewEntity;
import static com.example.interviewPrep.quiz.interview.dto.response.InterviewResponse.createInterviewResponse;

@Service
public class InterviewService {

    private final JwtService jwtService;
    private final InterviewRepository interviewRepository;
    private final MemberRepository memberRepository;

    public InterviewService(JwtService jwtService, InterviewRepository interviewRepository, MemberRepository memberRepository) {
        this.jwtService = jwtService;
        this.interviewRepository = interviewRepository;
        this.memberRepository = memberRepository;
    }


    public InterviewResponse createInterview(InterviewRequest interviewRequest) {

        Long memberId = jwtService.getMemberId();

        Member member = memberRepository.findById(memberId).orElse(null);

        if (member == null) {
            throw new CommonException(NOT_FOUND_MEMBER);
        }

        List<Answer> answers = interviewRequest.getAnswers();

        Interview interview = createInterviewEntity(member, answers);
        interviewRepository.save(interview);

        return createInterviewResponse(interview);
    }


}
