package com.example.interviewPrep.quiz.Member.domain;

import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.dto.Role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MemberTest {

    @Test
    @DisplayName("member create")
    void createMember(){

        String email = "hello@gmail.com";
        String password = "1234";
        String type = "user";
        String nickName = "tester";
        String name = "hello";
        String picture = "test.jpg";
        Role role = Role.USER;

        Member member1 = new Member(email, password, nickName);
        Member member2 = new Member(email, password, type, nickName, name, picture, role);

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
    @DisplayName("member update")
    void updateMember(){

        String newName = "new name";
        String newPicture = "nest_test.jpg";

        String email = "hello@gmail.com";
        String password = "1234";
        String nickName = "tester";

        Member member1 = new Member(email, password, nickName);
        member1.update(newName, newPicture);

        assertEquals(newName, member1.getName());
        assertEquals(newPicture, member1.getPicture());

    }

    @Test
    @DisplayName("get role key")
    void getRoleKey() {

        String email = "hello@gmail.com";
        String password = "1234";
        String type = "user";
        String nickName = "tester";
        String name = "hello";
        String picture = "test.jpg";
        Role role = Role.USER;

        Member member2 = new Member(email, password, type, nickName, name, picture, role);
        assertEquals(Role.USER.getKey(), member2.getRole().getKey());
    }

}