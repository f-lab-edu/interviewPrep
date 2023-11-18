package com.example.interviewPrep.quiz.email.controller;


import com.example.interviewPrep.quiz.email.service.EmailService;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.member.mentee.dto.request.MenteeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/service")
public class EmailController {
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
    @Autowired
    EmailService service;

    @PostMapping("/mail/change")
    @ResponseBody
    public void emailChangeConfirm(@RequestBody MenteeRequest menteeRequest) throws Exception {
        logger.info("post emailConfirm");
        Long memberId = JwtService.getMemberId();
        String email = menteeRequest.getEmail();
        service.sendSimpleMessage(email, "change");
    }


    @PostMapping("/mail/join")
    @ResponseBody
    public void emailJoinConfirm(@RequestBody MenteeRequest menteeRequest) throws Exception {
        logger.info("post emailConfirm");
        Long memberId = JwtService.getMemberId();
        String email = menteeRequest.getEmail();
        service.sendSimpleMessage(email, "join");
    }

}