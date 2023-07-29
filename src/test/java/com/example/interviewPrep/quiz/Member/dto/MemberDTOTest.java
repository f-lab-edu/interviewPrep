package com.example.interviewPrep.quiz.Member.dto;

import com.example.interviewPrep.quiz.member.dto.MemberDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberDTOTest {

    @Test
    @DisplayName("memberDTO create")
    void createMemberDTO(){
        Long id = 1L;
        String email = "hello@gmail.com";
        String password = "1234";
        String nickName = "tester";
        String newPassword = "5678";
        String type = "user";

        MemberDTO memberDTO = MemberDTO.builder()
                             .id(id)
                             .email(email)
                             .password(password)
                             .nickName(nickName)
                             .newPassword(newPassword)
                             .type(type)
                             .build();

        assertEquals(id, memberDTO.getId());
        assertEquals(email, memberDTO.getEmail());
        assertEquals(password, memberDTO.getPassword());
        assertEquals(nickName, memberDTO.getNickName());
        assertEquals(newPassword, memberDTO.getNewPassword());
        assertEquals(type, memberDTO.getType());
    }


    @Test
    @DisplayName("memberDTO create with null value")
    void createMemberDTOWithNullValue(){

        assertThrows(NullPointerException.class, () -> {
            MemberDTO.builder()
                .id(1L)
                .email(null)
                .password("password")
                .nickName("nickname")
                .newPassword("newpassword")
                .type("type")
                .build();
        });

    }

}