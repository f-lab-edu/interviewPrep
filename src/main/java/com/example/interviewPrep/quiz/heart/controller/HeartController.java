package com.example.interviewPrep.quiz.heart.controller;

import com.example.interviewPrep.quiz.heart.dto.HeartRequestDTO;
import com.example.interviewPrep.quiz.heart.service.HeartService;
import com.example.interviewPrep.quiz.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import static com.example.interviewPrep.quiz.utils.ResponseEntityConstants.RESPONSE_CREATED;
import static com.example.interviewPrep.quiz.utils.ResponseEntityConstants.RESPONSE_OK;
import static com.example.interviewPrep.quiz.utils.ResponseEntityConstants.RESPONSE_SERVER_ERROR;

@RestController
@RequestMapping("/heart")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Log4j2
public class HeartController {
    private final HeartService heartService;

    @PostMapping()
    public ResultResponse<?> create(@RequestBody @NotNull HeartRequestDTO heartDTO) throws InterruptedException {
        return ResultResponse.success(heartService.createHeart(heartDTO));
    }

    @DeleteMapping()
    public ResultResponse<?> delete(@RequestBody @NotNull HeartRequestDTO heartDTO) throws InterruptedException {
        return ResultResponse.success(heartService.deleteHeart(heartDTO));
    }


}
