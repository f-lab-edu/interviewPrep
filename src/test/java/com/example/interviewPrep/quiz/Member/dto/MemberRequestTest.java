package com.example.interviewPrep.quiz.Member.dto;

import com.example.interviewPrep.quiz.member.mentee.dto.request.MenteeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MenteeRequestTest {

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
    @DisplayName("MenteeRequest 생성")
    void createMenteeRequest() {
        String email = "hello@gmail.com";
        String password = "1234";
        String nickName = "tester";
        String newPassword = "5678";
        String type = "user";

        MenteeRequest menteeRequest = MenteeRequest.builder()
                .email(email)
                .password(password)
                .nickName(nickName)
                .build();

        assertEquals(email, menteeRequest.getEmail());
        assertEquals(password, menteeRequest.getPassword());
        assertEquals(nickName, menteeRequest.getNickName());
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    @DisplayName("MenteeRequest를 Null 값을 포함해서 생성")
    void createMenteeRequestWithNullValue(String email, String password, String nickName, String type) {
        assertThrows(NullPointerException.class, () -> {
            MenteeRequest.builder()
                    .email(email)
                    .password(password)
                    .nickName(nickName)
                    .build();
        });
    }

}