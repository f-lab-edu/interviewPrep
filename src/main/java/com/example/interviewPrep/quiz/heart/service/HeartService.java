package com.example.interviewPrep.quiz.heart.service;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.heart.domain.Heart;
import com.example.interviewPrep.quiz.heart.dto.request.HeartRequest;
import com.example.interviewPrep.quiz.heart.repository.HeartRepository;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;
import static com.example.interviewPrep.quiz.utils.JwtUtil.getMemberId;

@Service
public class HeartService {
    private final HeartRepository heartRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    public HeartService(HeartRepository heartRepository, AnswerRepository answerRepository, MemberRepository memberRepository) {
        this.heartRepository = heartRepository;
        this.answerRepository = answerRepository;
        this.memberRepository = memberRepository;
    }

    public void createHeart(HeartRequest heartRequest) throws InterruptedException {

        Long memberId = getMemberId();
        Long answerId = heartRequest.getAnswerId();

        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new CommonException(NOT_FOUND_ANSWER));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));

        Heart savedHeart = checkHeartExists(answerId, memberId);

        if (savedHeart != null) {
            throw new CommonException(EXIST_HEART_HISTORY);
        }

        increaseHeartCount(answerId);

        Heart heart = Heart.builder()
                .answer(answer)
                .member(member)
                .build();

        heartRepository.save(heart);
    }

    public Heart checkHeartExists(Long answerId, Long memberId) {
        return heartRepository.findByAnswerIdAndMemberId(answerId, memberId).orElse(null);
    }

    public void deleteHeart(HeartRequest heartRequest) throws InterruptedException {
        Long memberId = getMemberId();
        Long answerId = heartRequest.getAnswerId();

        Heart heart = checkHeartExists(answerId, memberId);

        if (heart == null) {
            throw new CommonException(NOT_EXIST_HEART_HISTORY);
        }

        decreaseHeartCount(answerId);
        heartRepository.delete(heart);
    }

    @Transactional
    public void increaseHeartWithOptimisticLock(Long answerId) {
        Answer answer = answerRepository.findByIdWithOptimisticLock(answerId).orElseThrow(() -> new CommonException(NOT_FOUND_ANSWER));
        answer.increase();
        answerRepository.save(answer);
    }

    @Transactional
    public void decreaseHeartWithOptimisticLock(Long answerId) {
        Answer answer = answerRepository.findByIdWithOptimisticLock(answerId).orElseThrow(() -> new CommonException(NOT_FOUND_ANSWER));
        answer.decrease();
        answerRepository.save(answer);
    }

    public void increaseHeartCount(Long answerId) throws InterruptedException {
        while (true) {
            try {
                increaseHeartWithOptimisticLock(answerId);
                break;
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }

    public void decreaseHeartCount(Long answerId) throws InterruptedException {
        while (true) {
            try {
                decreaseHeartWithOptimisticLock(answerId);
                break;
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }
}
