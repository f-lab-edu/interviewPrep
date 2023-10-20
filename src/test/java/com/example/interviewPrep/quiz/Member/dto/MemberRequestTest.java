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
                Arguments.of(null, "test@example.com", "password", "nickname", "type", "normal"),
                Arguments.of("tester", null, "password", "nickname", "type", "normal"),
                Arguments.of("tester", "test@example.com", null, "nickname", "type", "normal"),
                Arguments.of("tester", "test@example.com", "password", null, "type", "normal"),
                Arguments.of("tester", "test@example.com", "password", "nickName", null, "normal"),
                Arguments.of("tester", "test@example.com", "password", "nickName", "type", null)
        );
    }

    @Test
    @DisplayName("MemberRequest 생성")
    void createMemberRequest() {
        String name = "tester";
        String email = "hello@gmail.com";
        String password = "1234";
        String nickName = "tester";
        String type = "user";
        String role = "normal";

        MemberRequest memberRequest = MemberRequest.builder()
                .name(name)
                .email(email)
                .password(password)
                .nickName(nickName)
                .type(type)
                .role(role)
                .build();

        assertEquals(name, memberRequest.getName());
        assertEquals(email, memberRequest.getEmail());
        assertEquals(password, memberRequest.getPassword());
        assertEquals(nickName, memberRequest.getNickName());
        assertEquals(type, memberRequest.getType());
        assertEquals(role, memberRequest.getRole());
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    @DisplayName("MemberRequest를 Null 값을 포함해서 생성")
    void createMemberRequestWithNullValue(String name, String email, String password, String nickName, String type, String role) {
        assertThrows(NullPointerException.class, () -> {
            MemberRequest.builder()
                    .name(name)
                    .email(email)
                    .password(password)
                    .nickName(nickName)
                    .type(type)
                    .role(role)
                    .build();
        });
    }

}