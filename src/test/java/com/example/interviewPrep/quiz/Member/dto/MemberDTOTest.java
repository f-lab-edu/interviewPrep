package com.example.interviewPrep.quiz.Member.dto;

import com.example.interviewPrep.quiz.member.dto.MemberDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class MemberDTOTest {

    @Test
    @DisplayName("MemberDTO 생성")
    void create(){
        MemberDTO memberDTO = MemberDTO.builder()
                .id(1L)
                .email("hello@gmail.com")
                .password("1234")
                .build();

        assertThat(memberDTO.getId()).isEqualTo(1L);
        assertThat(memberDTO.getEmail()).isEqualTo("hello@gmail.com");
        assertThat(memberDTO.getPassword()).isEqualTo("1234");
    }


}