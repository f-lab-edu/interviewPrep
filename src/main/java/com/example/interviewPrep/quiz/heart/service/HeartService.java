package com.example.interviewPrep.quiz.heart.service;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.heart.dto.HeartRequestDTO;
import com.example.interviewPrep.quiz.heart.domain.Heart;
import com.example.interviewPrep.quiz.heart.repository.HeartRepository;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;
import static com.example.interviewPrep.quiz.utils.JwtUtil.*;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final AnswerRepository answerRepository;

    private final MemberRepository memberRepository;

    public boolean createHeart(HeartRequestDTO heartDTO) throws InterruptedException {
        Long memberId = getMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
            new CommonException(NOT_FOUND_MEMBER));
        if (heartRepository.findByAnswerIdAndMemberId(heartDTO.getAnswerId(), memberId).isPresent()) {
            throw new CommonException(EXIST_HEART_HISTORY);
        }

        increaseHeartFacade(heartDTO.getAnswerId());

        heartRepository.save(Heart.builder()
            .answer(answerRepository.findById(heartDTO.getAnswerId()).get())
            .member(member)
            .build());

        return true;
    }

    public boolean deleteHeart(HeartRequestDTO heartDTO) throws InterruptedException {
        Long memberId = getMemberId();
        Heart heart = heartRepository.findByAnswerIdAndMemberId(heartDTO.getAnswerId(), memberId).orElseThrow(() ->
            new CommonException(NOT_EXIST_HEART_HISTORY));

        decreaseHeartFacade(heartDTO.getAnswerId());

        heartRepository.delete(heart);

        return true;
    }

    @Transactional
    public void increaseHeartWithOptimisticLock(Long answerId) {
        Answer answer = answerRepository.findByIdWithOptimisticLock(answerId).orElseThrow(() ->
            new CommonException(NOT_FOUND_ANSWER));

        answer.increase();

        answerRepository.saveAndFlush(answer);
    }

    @Transactional
    public void decreaseHeartWithOptimisticLock(Long answerId) {
        Answer answer = answerRepository.findByIdWithOptimisticLock(answerId).orElseThrow(() ->
            new CommonException(NOT_FOUND_ANSWER));

        answer.decrease();

        answerRepository.saveAndFlush(answer);
    }

    public void increaseHeartFacade(Long answerId) throws InterruptedException {
        while (true) {
            try {
                increaseHeartWithOptimisticLock(answerId);
                break;
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }

    public void decreaseHeartFacade(Long answerId) throws InterruptedException {
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
