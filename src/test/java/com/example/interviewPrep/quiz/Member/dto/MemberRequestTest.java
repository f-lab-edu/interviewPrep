package com.example.interviewPrep.quiz.Member.dto;

import com.example.interviewPrep.quiz.member.dto.request.MemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberRequestTest {

    static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of(1L, null, "password", "nickname", "newpassword", "type"),
                Arguments.of(2L, "test@example.com", null, "nickname", "newpassword", "type"),
                Arguments.of(3L, "test@example.com", "password", null, "newpassword", "type"),
                Arguments.of(4L, "test@example.com", "password", "nickName", null, "type"),
                Arguments.of(5L, "test@example.com", "password", "nickName", "newpassword", null)
        );
    }

    @Test
    @DisplayName("MemberDTO 생성")
    void createMemberDTO() {
        String email = "hello@gmail.com";
        String password = "1234";
        String nickName = "tester";
        String newPassword = "5678";
        String type = "user";

        MemberRequest memberRequest = MemberRequest.builder()
                .email(email)
                .password(password)
                .nickName(nickName)
                .type(type)
                .build();

        assertEquals(email, memberRequest.getEmail());
        assertEquals(password, memberRequest.getPassword());
        assertEquals(nickName, memberRequest.getNickName());
        assertEquals(type, memberRequest.getType());
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    @DisplayName("MemberDTO를 Null 값을 포함해서 생성")
    void createMemberDTOWithNullValue(String email, String password, String nickName, String type) {
        assertThrows(NullPointerException.class, () -> {
            MemberRequest.builder()
                    .email(email)
                    .password(password)
                    .nickName(nickName)
                    .type(type)
                    .build();
        });
    }

}