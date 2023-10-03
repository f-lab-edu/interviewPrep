package com.example.interviewPrep.quiz.member.service;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.member.domain.MemberContext;
import com.example.interviewPrep.quiz.member.mentee.domain.Mentee;
import com.example.interviewPrep.quiz.member.mentee.repository.MenteeRepository;
import com.example.interviewPrep.quiz.member.mentor.domain.Mentor;
import com.example.interviewPrep.quiz.member.mentor.repository.MentorRepository;
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
    private final MentorRepository mentorRepository;

    @Override
    public UserDetails loadUserByUsername(String memberIdWithPrefix) throws CommonException {

        String prefix = memberIdWithPrefix.substring(0, 6);
        List<GrantedAuthority> type = new ArrayList<>();
        MemberContext memberContext;

        if(prefix.equals("Mentee")){
            Long menteeId = Long.parseLong(memberIdWithPrefix.substring(6));
            Mentee mentee = menteeRepository.findById(menteeId)
                                            .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));
            type.add(new SimpleGrantedAuthority("Mentee"));
            memberContext = new MemberContext(mentee, type);
            return memberContext;
        }

        Long mentorId = Long.parseLong(memberIdWithPrefix.substring(6));
        Mentor mentor = mentorRepository.findById(mentorId)
                                        .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));
        type.add(new SimpleGrantedAuthority("Mentor"));
        memberContext = new MemberContext(mentor, type);
        return memberContext;
    }
}