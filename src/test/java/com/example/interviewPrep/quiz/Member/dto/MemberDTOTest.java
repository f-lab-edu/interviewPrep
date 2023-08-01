package com.example.interviewPrep.quiz.Member.dto;

import com.example.interviewPrep.quiz.member.dto.MemberDTO;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberDTOTest {

    @Test
    @DisplayName("MemberDTO 생성")
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


    @ParameterizedTest
    @MethodSource("provideTestCases")
    @DisplayName("MemberDTO를 Null 값을 포함해서 생성")
    void createMemberDTOWithNullValue(long id, String email, String password, String nickName, String newPassword, String type) {
        assertThrows(NullPointerException.class, () -> {
            MemberDTO.builder()
                .id(id)
                .email(email)
                .password(password)
                .nickName(nickName)
                .newPassword(newPassword)
                .type(type)
                .build();
        });
    }

    static Stream<Arguments> provideTestCases() {
        return Stream.of(
            Arguments.of(1L, null, "password", "nickname", "newpassword", "type"),
            Arguments.of(2L, "test@example.com", null, "nickname", "newpassword", "type"),
            Arguments.of(3L, "test@example.com", "password", null, "newpassword", "type"),
            Arguments.of(4L, "test@example.com", "password", "nickName", null, "type"),
            Arguments.of(5L, "test@example.com", "password", "nickName", "newpassword", null)
        );
    }

}