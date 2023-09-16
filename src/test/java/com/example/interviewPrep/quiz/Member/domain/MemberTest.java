package com.example.interviewPrep.quiz.Member.domain;

import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.dto.Role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MemberTest {

    @Test
    @DisplayName("멤버 생성")
    void createMember(){

        String email = "hello@gmail.com";
        String password = "1234";
        String type = "user";
        String nickName = "tester";
        String name = "hello";
        String picture = "test.jpg";
        Role role = Role.USER;

        Member member1 = Member.builder()
                        .email(email)
                        .password(password)
                        .nickName(nickName)
                        .build();

        Member member2 = Member.builder()
                        .email(email)
                        .password(password)
                        .type(type)
                        .nickName(nickName)
                        .name(name)
                        .picture(picture)
                        .role(role)
                        .build();

        assertEquals(email, member1.getEmail());
        assertEquals(password, member1.getPassword());
        assertEquals(nickName, member1.getNickName());

        assertEquals(email, member2.getEmail());
        assertEquals(password, member2.getPassword());
        assertEquals(type, member2.getType());
        assertEquals(nickName, member2.getNickName());
        assertEquals(name, member2.getName());
        assertEquals(picture, member2.getPicture());
        assertEquals(role, member2.getRole());

    }


    @Test
    @DisplayName("멤버 업데이트")
    void updateMember(){

        String newName = "new name";
        String newPicture = "new_test.jpg";

        String email = "hello@gmail.com";
        String password = "1234";
        String nickName = "tester";

        Member member1 = Member.builder()
                        .email(email)
                        .password(password)
                        .nickName(nickName)
                        .build();

        member1.update(newName, newPicture);

        assertEquals(newName, member1.getName());
        assertEquals(newPicture, member1.getPicture());

    }

    @Test
    @DisplayName("Role key 가져오기")
    void getRoleKey() {

        String email = "hello@gmail.com";
        String password = "1234";
        String type = "user";
        String nickName = "tester";
        String name = "hello";
        String picture = "test.jpg";
        Role role = Role.USER;

        Member member2 = Member.builder()
                        .email(email)
                        .password(password)
                        .type(type)
                        .nickName(nickName)
                        .name(name)
                        .picture(picture)
                        .role(role)
                        .build();

        assertEquals(Role.USER.getKey(), member2.getRole().getKey());
    }

}