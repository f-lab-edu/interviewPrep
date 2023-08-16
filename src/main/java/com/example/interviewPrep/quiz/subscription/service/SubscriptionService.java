package com.example.interviewPrep.quiz.subscription.service;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.subscription.dto.request.SubscriptionRequest;
import com.example.interviewPrep.quiz.subscription.dto.response.SubscriptionResponse;
import com.example.interviewPrep.quiz.subscription.repository.SubscriptionRepository;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import org.springframework.stereotype.Service;
import com.example.interviewPrep.quiz.subscription.entity.Subscription;

import java.time.LocalDate;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final MemberRepository memberRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, MemberRepository memberRepository){
        this.subscriptionRepository = subscriptionRepository;
        this.memberRepository = memberRepository;
    }


    public SubscriptionResponse createSubscription(SubscriptionRequest subscriptionRequest){

        String type = subscriptionRequest.getType();

        if(!type.equals("create")){
            throw new CommonException(WRONG_SUBSCRIPTION_TYPE);
        }

        Long memberId = JwtUtil.getMemberId();
        Member member = memberRepository.findById(memberId).orElse(null);

        if(member == null){
            throw new CommonException(NOT_FOUND_MEMBER);
        }

        int monthDuration = subscriptionRequest.getMonthDuration();

        Subscription subscription = makeSubscription(member, monthDuration);

        subscriptionRepository.save(subscription);

        return SubscriptionResponse.builder()
                                    .monthDuration(monthDuration)
                                    .build();
    }


    public Subscription makeSubscription(Member member, int monthDuration){

        LocalDate startDate = LocalDate.now();
        int durationDays = monthDuration*30;
        LocalDate endDate = startDate.plusDays(durationDays);
        int totalFee = 0;

        if(monthDuration == 1){
            totalFee = monthDuration*9900;
        }else if(monthDuration == 3){
            totalFee = monthDuration*8900;
        }else if(monthDuration == 6){
            totalFee = monthDuration*7900;
        }else if(monthDuration == 12){
            totalFee = monthDuration*6900;
        }

        return Subscription.builder()
                            .member(member)
                            .startDate(startDate)
                            .endDate(endDate)
                            .totalFee(totalFee)
                            .isValid(true)
                            .build();
    }

    public SubscriptionResponse stopSubscription(SubscriptionRequest subscriptionRequest){

        String type = subscriptionRequest.getType();
        int monthDuration = subscriptionRequest.getMonthDuration();

        if(!type.equals("stop")){
            throw new CommonException(WRONG_SUBSCRIPTION_TYPE);
        }

        Long memberId = JwtUtil.getMemberId();
        Subscription subscription = subscriptionRepository.findByMemberId(memberId, true).orElse(null);

        if(subscription == null){
            throw new CommonException(NOT_FOUND_SUBSCRIPTION);
        }

        subscription.setIsValid(false);

        subscriptionRepository.save(subscription);

        return SubscriptionResponse.builder()
                .monthDuration(monthDuration)
                .build();
    }


}
