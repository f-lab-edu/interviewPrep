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
public class ChatRoom {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name="sender_id")
    Member sender;

    @ManyToOne
    @JoinColumn(name="receiver_id")
    Member receiver;


    @Builder
    public ChatRoom(Member sender, Member receiver){
        Objects.requireNonNull(sender, "Sender가 없습니다.");
        Objects.requireNonNull(receiver, "Receiver가 없습니다.");

        this.sender = sender;
        this.receiver = receiver;
    }


}
