package com.example.interviewPrep.quiz.email.controller;


import com.example.interviewPrep.quiz.email.service.EmailService;
import com.example.interviewPrep.quiz.email.service.EmailServiceImpl;
import com.example.interviewPrep.quiz.member.controller.MemberController;
import com.example.interviewPrep.quiz.member.dto.MemberDTO;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/service/")
public class EmailController {
    @Autowired
    EmailService service;

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @PostMapping("/mail/change")
    @ResponseBody
    public void emailChangeConfirm(@RequestBody MemberDTO memberDTO)throws Exception{
        logger.info("post emailConfirm");
        Long memberId = JwtUtil.getMemberId();
        String email = memberDTO.getEmail();
        service.sendSimpleMessage(email, "change");
    }


    @PostMapping("/mail/join")
    @ResponseBody
    public void emailJoinConfirm(@RequestBody MemberDTO memberDTO)throws Exception{
        logger.info("post emailConfirm");
        Long memberId = JwtUtil.getMemberId();
        String email = memberDTO.getEmail();
        service.sendSimpleMessage(email, "join");
    }

    @PostMapping("/verifyCode")
    @ResponseBody
    public int verifyCode(String code) {
        logger.info("Post verifyCode");

        int result = 0;
        System.out.println("code : "+code);
        System.out.println("code match : "+ EmailServiceImpl.ePw.equals(code));
        if(EmailServiceImpl.ePw.equals(code)) {
            result =1;
        }

        return result;
    }
}