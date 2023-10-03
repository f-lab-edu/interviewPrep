package com.example.interviewPrep.quiz.email.controller;


import com.example.interviewPrep.quiz.email.service.EmailService;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.member.controller.MemberController;
import com.example.interviewPrep.quiz.member.dto.request.MemberRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/service")
public class EmailController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private final JwtService jwtService;
    private final EmailService emailService;

    public EmailController(JwtService jwtService, EmailService emailService){
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    @PostMapping("/mail/change")
    @ResponseBody
    public void emailChangeConfirm(@RequestBody MemberRequest memberRequest) throws Exception {
        logger.info("post emailConfirm");
        jwtService.getMemberId();
        String email = memberRequest.getEmail();
        emailService.sendSimpleMessage(email, "change");
    }


    @PostMapping("/mail/join")
    @ResponseBody
    public void emailJoinConfirm(@RequestBody MemberRequest memberRequest) throws Exception {
        logger.info("post emailConfirm");
        jwtService.getMemberId();
        String email = memberRequest.getEmail();
        emailService.sendSimpleMessage(email, "join");
    }

}