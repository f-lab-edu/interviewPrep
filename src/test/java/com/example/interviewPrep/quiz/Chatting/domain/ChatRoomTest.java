package com.example.interviewPrep.quiz.Chatting.domain;

import com.example.interviewPrep.quiz.chatting.domain.ChatRoom;
import com.example.interviewPrep.quiz.member.domain.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ChatRoomTest {


    @Mock
    Member sender;

    @Mock
    Member receiver;


    @Test
    public void createChatRoom(){

        ChatRoom chatRoom = ChatRoom.builder()
                                    .sender(sender)
                                    .receiver(receiver)
                                    .build();

        assertThat(chatRoom.getSender()).isEqualTo(sender);
        assertThat(chatRoom.getReceiver()).isEqualTo(receiver);
    }



}
