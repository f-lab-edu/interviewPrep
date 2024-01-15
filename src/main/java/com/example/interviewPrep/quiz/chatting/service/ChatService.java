package com.example.interviewPrep.quiz.chatting.service;

import com.example.interviewPrep.quiz.chatting.domain.ChatRoom;
import com.example.interviewPrep.quiz.chatting.dto.response.ChatRoomResponse;
import com.example.interviewPrep.quiz.chatting.repository.ChatRoomRepository;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    private final ChatRoomRepository chatRoomRepository;

    public ChatService(JwtService jwtService, MemberRepository memberRepository, ChatRoomRepository chatRoomRepository){
        this.jwtService = jwtService;
        this.memberRepository = memberRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoomResponse createChatRoom(){

        Long memberId = jwtService.getMemberId();
        Member sender = memberRepository.findById(memberId).orElseThrow(null);
        Member receiver = memberRepository.findById(1L).orElseThrow(null);

        ChatRoom chatRoom = ChatRoom.builder()
                                    .sender(sender)
                                    .receiver(receiver)
                                    .build();

        chatRoomRepository.save(chatRoom);

        return ChatRoomResponse.builder()
                               .sender(sender)
                               .build();
    }


}
