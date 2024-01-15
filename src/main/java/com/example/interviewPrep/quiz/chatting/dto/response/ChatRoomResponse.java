package com.example.interviewPrep.quiz.chatting.dto.response;

import com.example.interviewPrep.quiz.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class ChatRoomResponse {


    Member sender;

    @Builder
    public ChatRoomResponse(Member sender){
        Objects.requireNonNull(sender, "sender가 null입니다.");
        this.sender = sender;
    }
}
