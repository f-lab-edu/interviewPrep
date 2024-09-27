package com.example.interviewPrep.quiz.Chatting.service;

import com.example.interviewPrep.quiz.chatting.dto.response.ChatRoomResponse;
import com.example.interviewPrep.quiz.chatting.repository.ChatRoomRepository;
import com.example.interviewPrep.quiz.chatting.service.ChatService;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {


    @Autowired
    ChatService chatService;

    @Mock
    JwtService jwtService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ChatRoomRepository chatRoomRepository;

    @Mock
    Member receiver;

    @BeforeEach
    public void setUp(){
        chatService = new ChatService(jwtService, memberRepository, chatRoomRepository);
    }


    @Test
    @DisplayName("채팅 룸 생성")
    public void createChatRoom(){

        Member sender = Member.builder()
                .id(2L)
                .email("abc@gmail.com")
                .name("abc")
                .password("1234")
                .build();

        given(jwtService.getMemberId()).willReturn(2L);
        given(memberRepository.findById(2L)).willReturn(Optional.ofNullable(sender));
        given(memberRepository.findById(1L)).willReturn(Optional.ofNullable(receiver));

        ChatRoomResponse chatRoomResponse = chatService.createChatRoom();

        assertThat(chatRoomResponse.getSender()).isEqualTo(sender);
    }



}
