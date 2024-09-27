package com.example.interviewPrep.quiz.chatting.repository;

import com.example.interviewPrep.quiz.chatting.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
