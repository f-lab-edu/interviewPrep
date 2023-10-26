package com.example.interviewPrep.quiz.Chatting.dto.response;


import com.example.interviewPrep.quiz.chatting.dto.response.ChatRoomResponse;
import com.example.interviewPrep.quiz.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ChatRoomResponseTest {


    @Mock
    Member sender;

    @Test
    @DisplayName("ChatRoomResponse 생성")
    public void createChatRoomResponse(){

        ChatRoomResponse chatRoomResponse = ChatRoomResponse.builder()
                                            .sender(sender)
                                            .build();

        assertThat(chatRoomResponse.getSender()).isEqualTo(sender);

    }



}
