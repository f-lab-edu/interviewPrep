package com.example.interviewPrep.quiz.Member.repository;

import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MemberRepositoryTest {

    @Mock
    private MemberRepository memberRepository;


    Member member;
    String email;
    String password;
    String type;

    @BeforeEach
    void setUp(){
        // Given
        email = "hello@gmail.com";
        password = "1234";
        type = "google";

        member = Member.builder()
                .email(email)
                .password(password)
                .type(type)
                .build();

    }


    @Test
    @DisplayName("회원을 DB에 저장")
    public void save(){
        given(memberRepository.save(member)).willReturn(member);

        // When
        Member savedMember = memberRepository.save(member);

        // Then
        String savedEmail = savedMember.getEmail();

        assertEquals(savedEmail, member.getEmail());
    }
    
    @Test
    @DisplayName("Email로 회원 찾기")
    public void findByEmail(){
        given(memberRepository.save(member)).willReturn(member);
        given(memberRepository.findByEmail(email)).willReturn(Optional.ofNullable(member));

        // When
        memberRepository.save(member);
        Optional<Member> searchedMember = memberRepository.findByEmail(email);

        // Then
        assertThat(email).isEqualTo(searchedMember.get().getEmail());
    }

     


}
