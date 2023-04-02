package com.example.interviewPrep.quiz.Member.domain;

import com.example.interviewPrep.quiz.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    @DisplayName("회원을 Id 포함해서 생성")
    void creationWithId(){
        Member member = Member.builder()
                .id(11L)
                .email("hello@gmail.com")
                .password("1234")
                .build();

        assertThat(member.getId()).isEqualTo(11L);
        assertThat(member.getEmail()).isEqualTo("hello@gmail.com");
        assertThat(member.getPassword()).isEqualTo("1234");
    }


    @Test
    @DisplayName("회원을 Id 없이 생성")
    void creationWithoutId(){
        Member member = Member.builder()
                .email("hello@gmail.com")
                .password("1234")
                .build();

        assertThat(member.getEmail()).isEqualTo("hello@gmail.com");
        assertThat(member.getPassword()).isEqualTo("1234");
    }

}