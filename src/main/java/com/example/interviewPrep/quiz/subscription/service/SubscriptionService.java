package com.example.interviewPrep.quiz.subscription.service;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.subscription.dto.request.SubscriptionRequest;
import com.example.interviewPrep.quiz.subscription.dto.response.SubscriptionResponse;
import com.example.interviewPrep.quiz.subscription.entity.Subscription;
import com.example.interviewPrep.quiz.subscription.repository.SubscriptionRepository;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import com.example.interviewPrep.quiz.utils.MonthDuration;
import com.example.interviewPrep.quiz.utils.MonthlyFee;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_MEMBER;
import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_SUBSCRIPTION;
import static com.example.interviewPrep.quiz.subscription.dto.response.SubscriptionResponse.createSubscriptionResponse;
import static com.example.interviewPrep.quiz.subscription.entity.Subscription.createSubscriptionEntity;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final MemberRepository memberRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, MemberRepository memberRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.memberRepository = memberRepository;
    }


    public SubscriptionResponse createSubscription(SubscriptionRequest subscriptionRequest) {

        Long memberId = JwtUtil.getMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));

        int monthDuration = subscriptionRequest.getMonthDuration();

        Subscription subscription = makeSubscription(member, monthDuration);

        subscriptionRepository.save(subscription);

        member.setIsPaid(true);
        memberRepository.save(member);

        return createSubscriptionResponse(monthDuration);
    }


    public Subscription makeSubscription(Member member, int monthDuration) {

        LocalDate startDate = LocalDate.now();
        int durationDays = monthDuration * 30;
        LocalDate endDate = startDate.plusDays(durationDays);

        MonthDuration months = MonthDuration.valueOf(Integer.toString(monthDuration));
        int totalFee = 0;

        if (months.equals(MonthDuration.ONE)) {
            totalFee = monthDuration * MonthlyFee.ONE.getCosts();
        } else if (months.equals(MonthDuration.THREE)) {
            totalFee = monthDuration * MonthlyFee.THREE.getCosts();
        } else if (months.equals(MonthDuration.SIX)) {
            totalFee = monthDuration * MonthlyFee.SIX.getCosts();
        } else if (months.equals(MonthDuration.TWELVE)) {
            totalFee = monthDuration * MonthlyFee.TWELVE.getCosts();
        }

        return createSubscriptionEntity(member, startDate, endDate, totalFee, true);
    }

    public void stopSubscription() {

        Long memberId = JwtUtil.getMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));

        Subscription subscription = subscriptionRepository.findByMemberId(memberId, true).orElseThrow(() -> new CommonException(NOT_FOUND_SUBSCRIPTION));

        subscription.cancel(false);

        subscriptionRepository.save(subscription);

        member.setIsPaid(false);
        memberRepository.save(member);
    }


}
