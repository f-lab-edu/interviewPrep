package com.example.interviewPrep.quiz.Member.domain;

import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.dto.Role;

import com.example.interviewPrep.quiz.member.mentee.domain.Mentee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MenteeTest {

    @Test
    @DisplayName("멤버 생성")
    void createMentee(){

        String email = "hello@gmail.com";
        String password = "1234";
        String type = "user";
        String nickName = "tester";
        String name = "hello";
        String picture = "test.jpg";
        Role role = Role.USER;

        Mentee mentee1 = Mentee.builder()
                        .email(email)
                        .password(password)
                        .nickName(nickName)
                        .build();

        Mentee mentee2 = Mentee.builder()
                        .email(email)
                        .password(password)
                        .type(type)
                        .nickName(nickName)
                        .name(name)
                        .picture(picture)
                        .build();

        assertEquals(email, mentee1.getEmail());
        assertEquals(password, mentee1.getPassword());
        assertEquals(nickName, mentee1.getNickName());

        assertEquals(email, mentee2.getEmail());
        assertEquals(password, mentee2.getPassword());
        assertEquals(type, mentee2.getType());
        assertEquals(nickName, mentee2.getNickName());
        assertEquals(name, mentee2.getName());
        assertEquals(picture, mentee2.getPicture());
    }


    @Test
    @DisplayName("멘티 업데이트")
    void updateMentee(){

        String newName = "new name";
        String newPicture = "new_test.jpg";

        String email = "hello@gmail.com";
        String password = "1234";
        String nickName = "tester";

        Mentee mentee1 = Mentee.builder()
                        .email(email)
                        .password(password)
                        .nickName(nickName)
                        .build();

        mentee1.update(newName, newPicture);

        assertEquals(newName, mentee1.getName());
        assertEquals(newPicture, mentee1.getPicture());

    }


}