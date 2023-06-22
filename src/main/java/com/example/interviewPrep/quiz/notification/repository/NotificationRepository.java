package com.example.interviewPrep.quiz.notification.repository;

import java.util.List;

import com.example.interviewPrep.quiz.notification.domain.Notification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceiverId(Long id);
}