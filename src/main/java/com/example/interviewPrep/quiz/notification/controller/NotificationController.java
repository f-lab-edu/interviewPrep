package com.example.interviewPrep.quiz.notification.controller;

import com.example.interviewPrep.quiz.notification.service.NotificationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * @title 로그인 한 유저 sse 연결
     */
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@RequestParam(value = "lastEventId", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(lastEventId);
    }


    /**
     * @title 로그인 한 유저의 모든 알림 조회
     */
    /*
    @GetMapping("/notifications")
    public ResponseEntity<NotificationsResponse> notifications(@Login LoginMember loginMember) {
        return ResponseEntity.ok().body(notificationService.findAllById(loginMember));
    }

    /**
     * @title 알림 읽음 상태 변경
     */
    /*
    @PatchMapping("/notifications/{id}")
    public ResponseEntity<Void> readNotification(@PathVariable Long id) {
        notificationService.readNotification(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    */
}