package com.example.interviewPrep.quiz.Chatting.domain;

import com.example.interviewPrep.quiz.chatting.domain.ChatMessage;
import com.example.interviewPrep.quiz.chatting.domain.ChatRoom;
import com.example.interviewPrep.quiz.member.domain.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@ExtendWith(MockitoExtension.class)
public class ChatMessageTest {



    @Mock
    Member sender;

    @Mock
    ChatRoom chatRoom;


    @Test
    public void createChatMessage(){

        ChatMessage chatMessage = ChatMessage.builder()
                                .content("hello")
                                .sender(sender)
                                .chatRoom(chatRoom)
                                .build();


        assertThat(chatMessage.getContent()).isEqualTo("hello");
        assertThat(chatMessage.getSender()).isEqualTo(sender);
        assertThat(chatMessage.getChatRoom()).isEqualTo(chatRoom);

    }


}
