package com.example.interviewPrep.quiz.utils;

import com.example.interviewPrep.quiz.member.mentee.domain.Mentee;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;

@Component
public class CreatePassword {
    public String createPwd() {
        String password = new BigInteger(10, new SecureRandom()).toString();
        return SHA256Util.encryptSHA256(password);
    }

}
