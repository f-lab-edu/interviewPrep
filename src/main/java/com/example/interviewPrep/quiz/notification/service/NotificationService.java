package com.example.interviewPrep.quiz.notification.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;


import com.example.interviewPrep.quiz.answer.domain.AnswerComment;
import com.example.interviewPrep.quiz.dto.NotificationResponse;
import com.example.interviewPrep.quiz.dto.NotificationsResponse;
import com.example.interviewPrep.quiz.emitter.repository.EmitterRepository;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.notification.domain.Notification;
import com.example.interviewPrep.quiz.notification.repository.NotificationRepository;
import com.example.interviewPrep.quiz.redis.RedisDao;
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

    private final MemberRepository memberRepository;
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    private final RedisDao redisDao;

    public NotificationService(MemberRepository memberRepository, EmitterRepository emitterRepository, NotificationRepository notificationRepository, RedisDao redisDao) {
        this.memberRepository = memberRepository;
        this.emitterRepository = emitterRepository;
        this.notificationRepository = notificationRepository;
        this.redisDao = redisDao;
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

        String SseId = "Sse"+ id;

        List<Notification> notifications = redisDao.getValuesForNotification(SseId);
        if(notifications == null){
            redisDao.setValuesForNotification(SseId);
        }else{
            for(Notification notification: notifications){
                sendToClient(emitter, id, notification);
            }
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


    @Transactional
    public void send(Member receiver, AnswerComment comment, String content) {
        Notification notification = createNotification(receiver, comment, content);
        String id = String.valueOf(receiver.getId());
        notificationRepository.save(notification);

        // Emitter의 존재 여부를 확인하고,
        // Emitter 존재 시 notification 발송
        checkEmitterAndSendToClient(id, notification);

    }

    public void checkEmitterAndSendToClient(String id, Notification notification){

        Optional<SseEmitter> emitter = emitterRepository.findById(id);

        // emitterRepository.showAllKeys();

        // emitter가 null이 아닌 경우,
        // 즉, receiver가 현재 로그인된 경우, receiver에게 바로 알림을 전송
        if(emitter.isPresent()){
            SseEmitter receiverEmitter = emitter.get();
            sendToClient(receiverEmitter, id, notification);
            return;
        }

        // emitter가 null인 경우,
        // 즉, receiver가 현재 로그인되지 않은 경우,
        // redisDao에서 SseId에 해당하는 notifications를 찾고 업데이트

        String SseId = "Sse" + id;
        List<Notification> notifications = redisDao.getValuesForNotification(SseId);

        // receiver가 한 번도 로그인하지 않은 경우,
        // Redis에 SseId에 대응되는 notifications가 없으므로,
        // 새로운 ArrayList를 추가한다
        if(notifications == null){
            notifications = new ArrayList<>();
        }
        notifications.add(notification);
        redisDao.updateValuesForNotification(SseId, notifications);
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