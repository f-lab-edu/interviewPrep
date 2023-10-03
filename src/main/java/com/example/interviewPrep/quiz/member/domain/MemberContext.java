package com.example.interviewPrep.quiz.member.domain;

import com.example.interviewPrep.quiz.member.mentor.domain.Mentor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import com.example.interviewPrep.quiz.member.mentee.domain.Mentee;

import java.util.Collection;

public class MemberContext extends User {

    private Mentor mentor;
    private Mentee mentee;

    public MemberContext(Mentee mentee, Collection<? extends GrantedAuthority> authorities){
        super(Long.toString(mentee.getId()), mentee.getPassword(), authorities);
        this.mentee = mentee;
    }

    public MemberContext(Mentor mentor, Collection<? extends GrantedAuthority> authorities){
        super(Long.toString(mentor.getId()), mentor.getPassword(), authorities);
        this.mentor = mentor;
    }

}
