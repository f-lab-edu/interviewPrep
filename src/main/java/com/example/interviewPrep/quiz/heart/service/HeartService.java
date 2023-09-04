package com.example.interviewPrep.quiz.heart.service;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.heart.domain.Heart;
import com.example.interviewPrep.quiz.heart.dto.request.HeartRequest;
import com.example.interviewPrep.quiz.heart.repository.AnswerLockRepository;
import com.example.interviewPrep.quiz.heart.repository.HeartRepository;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;

@Service
public class HeartService {
    private final HeartRepository heartRepository;
    private final AnswerLockRepository answerLockRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    public HeartService(HeartRepository heartRepository, AnswerLockRepository answerLockRepository, AnswerRepository answerRepository, MemberRepository memberRepository) {
        this.heartRepository = heartRepository;
        this.answerLockRepository = answerLockRepository;
        this.answerRepository = answerRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void createHeart(HeartRequest heartRequest) {

        Long memberId = JwtUtil.getMemberId();
        Long answerId = heartRequest.getAnswerId();

        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new CommonException(NOT_FOUND_ANSWER));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));

        if (checkHeartExists(answerId, memberId)) {
            throw new CommonException(EXIST_HEART_HISTORY);
        }

        increaseAnswerHeartCntWithNamedLock(answerId);

        Heart heart = Heart.builder()
                .answer(answer)
                .member(member)
                .build();

        heartRepository.save(heart);
    }

    public boolean checkHeartExists(Long answerId, Long memberId) {
        Optional<Heart> heart = heartRepository.findByAnswerIdAndMemberId(answerId, memberId);
        return heart.isPresent();
    }

    @Transactional
    public void deleteHeart(HeartRequest heartRequest) {
        Long memberId = JwtUtil.getMemberId();
        Long answerId = heartRequest.getAnswerId();

        if (!checkHeartExists(answerId, memberId)) {
            throw new CommonException(NOT_EXIST_HEART_HISTORY);
        }

        decreaseAnswerHeartCntWithNamedLock(answerId);

        Heart heart = heartRepository.findByAnswerIdAndMemberId(answerId, memberId).get();
        heartRepository.delete(heart);
    }

    public void increaseAnswerHeartCntWithNamedLock(Long answerId) {

        try {
            answerLockRepository.getLock(answerId.toString());
            increaseAnswerHeartCnt(answerId);
        } finally {
            answerLockRepository.releaseLock(answerId.toString());
        }
    }

    public void increaseAnswerHeartCnt(Long answerId) {
        Answer answer = findAnswer(answerId);
        answer.increase();
    }

    public void decreaseAnswerHeartCntWithNamedLock(Long answerId) {
        try {
            answerLockRepository.getLock(answerId.toString());
            decreaseAnswerHeartCnt(answerId);
        } finally {
            answerLockRepository.releaseLock(answerId.toString());
        }
    }

    public void decreaseAnswerHeartCnt(Long answerId) {
        Answer answer = findAnswer(answerId);
        answer.decrease();
    }

    public Answer findAnswer(Long answerId) {
        return answerRepository.findById(answerId).orElseThrow(() -> new CommonException(NOT_FOUND_ANSWER));
    }

}
