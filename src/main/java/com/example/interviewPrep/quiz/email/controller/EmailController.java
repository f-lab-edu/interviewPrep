package com.example.interviewPrep.quiz.email.controller;


import com.example.interviewPrep.quiz.email.service.EmailService;
import com.example.interviewPrep.quiz.member.controller.MemberController;
import com.example.interviewPrep.quiz.member.dto.request.MemberRequest;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/service")
public class EmailController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    @Autowired
    EmailService service;

    @PostMapping("/mail/change")
    @ResponseBody
    public void emailChangeConfirm(@RequestBody MemberRequest memberRequest) throws Exception {
        logger.info("post emailConfirm");
        Long memberId = JwtUtil.getMemberId();
        String email = memberRequest.getEmail();
        service.sendSimpleMessage(email, "change");
    }


    @PostMapping("/mail/join")
    @ResponseBody
    public void emailJoinConfirm(@RequestBody MemberRequest memberRequest) throws Exception {
        logger.info("post emailConfirm");
        Long memberId = JwtUtil.getMemberId();
        String email = memberRequest.getEmail();
        service.sendSimpleMessage(email, "join");
    }

}