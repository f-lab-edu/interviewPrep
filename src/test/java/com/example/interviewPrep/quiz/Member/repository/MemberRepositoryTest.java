package com.example.interviewPrep.quiz.Member.repository;

import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private final MemberRepository memberRepository = mock(MemberRepository.class);


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

        // When
        memberRepository.save(member);

        // Then
        String savedEmail = member.getEmail();

        assertEquals(member, memberRepository.findByEmail(savedEmail).get());
        assertEquals(member, memberRepository.findByEmailAndType(savedEmail,type).get());
    }
    
    @Test
    @DisplayName("Email로 회원 찾기")
    public void findByEmail(){

        // When
        memberRepository.save(member);
        Optional<Member> searchedMember = memberRepository.findByEmail(email);

        // Then
        assertThat(email).isEqualTo(searchedMember.get().getEmail());
    }

     


}
