package com.example.interviewPrep.quiz.member.service;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.member.domain.MemberContext;
import com.example.interviewPrep.quiz.member.mentee.domain.Mentee;
import com.example.interviewPrep.quiz.member.mentee.repository.MenteeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_MEMBER;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService{

    private final MenteeRepository menteeRepository;

    @Override
    public UserDetails loadUserByUsername(String menteeId) throws CommonException {

        List<GrantedAuthority> type = new ArrayList<>();
        MemberContext memberContext;

        Mentee mentee = menteeRepository.findById(Long.parseLong(menteeId))
                                            .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));
        type.add(new SimpleGrantedAuthority("Mentee"));
        memberContext = new MemberContext(mentee, type);
        return memberContext;
    }
}