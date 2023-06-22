package com.example.interviewPrep.quiz.notification.service;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;


import com.example.interviewPrep.quiz.answer.domain.AnswerComment;
import com.example.interviewPrep.quiz.dto.NotificationResponse;
import com.example.interviewPrep.quiz.dto.NotificationsResponse;
import com.example.interviewPrep.quiz.emitter.repository.EmitterRepository;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.notification.domain.Notification;
import com.example.interviewPrep.quiz.notification.repository.NotificationRepository;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    public NotificationService(EmitterRepository emitterRepository, NotificationRepository notificationRepository) {
        this.emitterRepository = emitterRepository;
        this.notificationRepository = notificationRepository;
    }

    public SseEmitter subscribe(String lastEventId) {

        Long memberId = JwtUtil.getMemberId();

        String id = Long.toString(memberId);
        // String id = memberId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, id, "EventStream Created. [memberId=" + memberId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(memberId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            log.error("SSE 연결 오류!", exception);
            System.out.println(exception);
        }
    }


    private void sendAllToClient(SseEmitter emitter, String id, List<Notification> notifications) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(notifications));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            log.error("SSE 연결 오류!", exception);
            System.out.println(exception);
        }
    }

    public void sendAll(Long memberId, List<Notification> notifications){

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllById(Long.toString(memberId));
        sseEmitters.forEach(
                (key, emitter) -> {
                    sendAllToClient(emitter, key, notifications);
                }
        );
    }

    @Transactional
    public void send(Member receiver, AnswerComment comment, String content) {
        Notification notification = createNotification(receiver, comment, content);
        String id = String.valueOf(receiver.getId());
        notificationRepository.save(notification);
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllById(id);

        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendToClient(emitter, key, NotificationResponse.from(notification));
                }
        );
    }

    private Notification createNotification(Member receiver, AnswerComment comment, String content) {
        return Notification.builder()
                .receiver(receiver)
                .comment(comment)
                .content(content)
                .isRead(false)
                .build();
    }

    @Transactional
    public NotificationsResponse findAllById() {

        Long memberId = JwtUtil.getMemberId();

        List<NotificationResponse> responses = notificationRepository.findAllByReceiverId(memberId).stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
        long unreadCount = responses.stream()
                .filter(notification -> !notification.isRead())
                .count();

        return NotificationsResponse.of(responses, unreadCount);
    }

    @Transactional
    public void readNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 알림입니다."));
        notification.read();
    }
}