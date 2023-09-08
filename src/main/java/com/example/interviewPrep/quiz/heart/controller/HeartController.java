package com.example.interviewPrep.quiz.heart.controller;

import com.example.interviewPrep.quiz.heart.dto.request.HeartRequest;
import com.example.interviewPrep.quiz.heart.service.HeartService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/hearts")
@CrossOrigin(origins = "*")
@Log4j2
public class HeartController {
    private final HeartService heartService;

    public HeartController(HeartService heartService) {
        this.heartService = heartService;
    }

    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody HeartRequest heartRequest) throws InterruptedException {
        heartService.createHeart(heartRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestBody @NotNull HeartRequest heartRequest) throws InterruptedException {
        heartService.deleteHeart(heartRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
