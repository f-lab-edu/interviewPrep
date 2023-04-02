package com.example.interviewPrep.quiz.heart.service;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.exception.advice.ErrorCode;
import com.example.interviewPrep.quiz.heart.domain.RefHeart;
import com.example.interviewPrep.quiz.heart.repository.RefHeartRepository;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.question.domain.QuestionReference;
import com.example.interviewPrep.quiz.question.repository.ReferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.interviewPrep.quiz.utils.JwtUtil.getMemberId;

@Service
@RequiredArgsConstructor
public class RefHeartService {
    private final ReferenceRepository referenceRepository;
    private final RefHeartRepository refHeartRepository;
    private final MemberRepository memberRepository;

    public boolean createRefHeart(Long referenceId) {
        QuestionReference reference = referenceRepository.findById(referenceId).orElseThrow(() ->
            new CommonException(ErrorCode.NOT_FOUND_REF));
        Member member = memberRepository.findById(getMemberId()).orElseThrow(() ->
            new CommonException(ErrorCode.NOT_FOUND_ID));

        if (refHeartRepository.findByReferenceIdAndMemberId(referenceId, getMemberId()).isPresent()) {
            throw new CommonException(ErrorCode.EXIST_HEART_HISTORY);
        }

        refHeartRepository.save(RefHeart.builder().reference(reference).member(member).build());
        return true;
    }

    public boolean deleteRefHeart(Long referenceId) {
        RefHeart refHeart = refHeartRepository.findByReferenceIdAndMemberId(referenceId, getMemberId()).orElseThrow(() ->
            new CommonException(ErrorCode.NOT_EXIST_HEART_HISTORY));

        refHeartRepository.deleteById(refHeart.getId());

        return true;
    }
}
