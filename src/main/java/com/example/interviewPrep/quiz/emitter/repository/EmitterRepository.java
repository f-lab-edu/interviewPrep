package com.example.interviewPrep.quiz.emitter.repository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepository {

    public final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    public SseEmitter save(String id, SseEmitter sseEmitter) {
        emitters.put(id, sseEmitter);
        return sseEmitter;
    }
    public Optional<SseEmitter> findById(String id) {
        Optional<SseEmitter> emitter = Optional.ofNullable(emitters.get(id));
        return emitter;
    }


    public void deleteById(String id) {
        emitters.remove(id);
    }

}