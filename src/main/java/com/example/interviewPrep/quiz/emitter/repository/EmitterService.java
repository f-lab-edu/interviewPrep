package com.example.interviewPrep.quiz.emitter.repository;

import org.springframework.stereotype.Service;

@Service
public class EmitterService {

      private final EmitterRepository emitterRepository;

      public EmitterService(EmitterRepository emitterRepository){
        this.emitterRepository = emitterRepository;
      }

      public void deleteMemberEmitter(String memberId){
    emitterRepository.deleteById(memberId);
  }

}
