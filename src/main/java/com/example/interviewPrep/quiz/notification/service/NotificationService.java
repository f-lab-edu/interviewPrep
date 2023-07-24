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
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    private final RedisDao redisDao;

    public NotificationService(EmitterRepository emitterRepository, NotificationRepository notificationRepository, RedisDao redisDao) {
        this.emitterRepository = emitterRepository;
        this.notificationRepository = notificationRepository;
        this.redisDao = redisDao;
    }

    public SseEmitter subscribe() {

        Long memberId = JwtUtil.getMemberId();
        String id = Long.toString(memberId);
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, id, "EventStream Created. [memberId=" + memberId + "]");

        String SseId = "Sse"+ id;

        List<Notification> notifications = redisDao.getValuesForNotification(SseId);
        if(notifications == null){
            redisDao.setValuesForNotification(SseId);
            return emitter;
        }

        for(Notification notification: notifications) {
            sendToClient(emitter, id, notification);
            notification.read();
            notificationRepository.save(notification);
        }

        redisDao.deleteValuesForNotification(SseId);
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
        }
    }


    @Transactional
    public void send(Member receiver, AnswerComment comment, String content) {
        Notification notification = createNotification(receiver, comment, content);
        String id = String.valueOf(receiver.getId());
        notification.createReceiverMemberId(id);
        notificationRepository.save(notification);

        // Emitter의 존재 여부를 확인하고,
        // Emitter 존재 시 notification 발송
        checkEmitterAndSendToClient(id, notification);

    }



    public void checkEmitterAndSendToClient(String id, Notification notification){

        Optional<SseEmitter> emitter = emitterRepository.findById(id);

        if(emitter.isPresent()){
            SseEmitter receiverEmitter = emitter.get();
            sendToClient(receiverEmitter, id, notification);
            notification.read();
            notificationRepository.save(notification);
            return;
        }

        String SseId = "Sse" + id;
        redisDao.updateValuesForNotification(SseId, notification);
    }


    private Notification createNotification(Member receiver, AnswerComment comment, String content) {
        return new Notification(receiver, comment, content, false);
    }

}