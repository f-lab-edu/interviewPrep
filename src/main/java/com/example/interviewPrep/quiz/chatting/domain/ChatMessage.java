package com.example.interviewPrep.quiz.chatting.domain;

import com.example.interviewPrep.quiz.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
public class ChatMessage {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name="sender_id")
    Member sender;

    String content;

    @ManyToOne
    @JoinColumn(name="room_id")
    ChatRoom chatRoom;

    @Builder
    public ChatMessage(Member sender, String content, ChatRoom chatRoom){
        Objects.requireNonNull(sender, "Sender가 없습니다.");
        Objects.requireNonNull(content, "Message가 없습니다.");
        Objects.requireNonNull(chatRoom, "ChatRoom이 없습니다.");

        this.sender = sender;
        this.content = content;
        this.chatRoom = chatRoom;
    }


}
