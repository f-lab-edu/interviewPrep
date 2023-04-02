package com.example.interviewPrep.quiz.Member.service;

import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.dto.MemberDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {
    private Member member;
    private MemberDTO memberDTO;

    String email;
    String password;
    String type;

    @BeforeEach
    void setUp(){
        memberDTO = MemberDTO.builder()
                .email("hello@gmail.com")
                .password("1234")
                .type("mentee")
                .build();

        email = memberDTO.getEmail();
        password = memberDTO.getPassword();
        type = memberDTO.getType();
    }

    @Test
    @DisplayName("멤버 로그인")
    void loginByEmailAndPassword() {

        member = Member.builder()
                .email(email)
                .password(password)
                .type(type)
                .build();

        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getPassword()).isEqualTo(password);
        assertThat(member.getType()).isEqualTo(type);
    }

}
