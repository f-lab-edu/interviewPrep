package com.example.interviewPrep.quiz.member.mentee.service;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.member.mentee.domain.Mentee;
import com.example.interviewPrep.quiz.member.mentee.dto.request.MenteeRequest;
import com.example.interviewPrep.quiz.member.mentee.dto.response.MenteeResponse;
import com.example.interviewPrep.quiz.member.mentee.repository.MenteeRepository;
import com.example.interviewPrep.quiz.redis.RedisDao;
import com.example.interviewPrep.quiz.utils.AES256;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;
import static com.example.interviewPrep.quiz.member.mentee.dto.response.MenteeResponse.createMenteeResponse;

@Service
public class MenteeService {
    private final MenteeRepository menteeRepository;
    private final RedisDao redisDao;

    public MenteeService(MenteeRepository menteeRepository, RedisDao redisDao) {
        this.menteeRepository = menteeRepository;
        this.redisDao = redisDao;
    }


    public void createMentee(MenteeRequest menteeRequest) {

        AES256 aes256 = new AES256();
        String email = menteeRequest.getEmail();

        if (isDuplicatedEmail(email)) {
            throw new CommonException(DUPLICATE_EMAIL);
        }

        Mentee mentee = MenteeRequest.createMentee(menteeRequest);
        menteeRepository.save(mentee);
    }

    public boolean isDuplicatedEmail(String email) {
        Optional<Mentee> mentee = menteeRepository.findByEmail(email);
        return mentee.isPresent();
    }

    public boolean isDuplicatedNickName(String nickName) {
        Optional<Mentee> mentee = menteeRepository.findByNickName(nickName);
        return mentee.isPresent();
    }

    public MenteeResponse getUserInfo() {
        Long id = JwtUtil.getMemberId();
        Mentee mentee = menteeRepository.findById(id).orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));
        mentee.setPassword(null);
        return createMenteeResponse(mentee);
    }

    public void updateNickName(Mentee mentee, String newNickName) {
        if (isDuplicatedNickName(newNickName)) {
            throw new CommonException(DUPLICATE_EMAIL);
        }
        mentee.setNickName(newNickName);
    }

    public void updateEmail(Mentee mentee, String newEmail) {
        if (isDuplicatedEmail(newEmail)) {
            throw new CommonException(DUPLICATE_EMAIL);
        }
        mentee.setEmail(newEmail);
    }


    public MenteeResponse updatePassword(MenteeRequest menteeRequest) {

        Long menteeId = JwtUtil.getMemberId();

        Mentee mentee = menteeRepository.findById(menteeId).orElseThrow(() -> new CommonException(NOT_FOUND_MENTEE));
        String password = mentee.getPassword();

        String inputPassword = menteeRequest.getPassword();
        String hashedInputPassword = SHA256Util.encryptSHA256(inputPassword);
        String newPassword = menteeRequest.getNewPassword();
        String hashedNewPassword = SHA256Util.encryptSHA256(newPassword);

        if (!isValidPassword(mentee, password, hashedInputPassword, hashedNewPassword)) {
            throw new CommonException(WRONG_PASSWORD);
        }

        return createMenteeResponse(mentee);
    }

    public boolean isValidPassword(Mentee mentee, String password, String hashedInputPassword, String hashedNewPassword) {
        if (password.equals(hashedInputPassword)) {
            mentee.setPassword(hashedNewPassword);
            menteeRepository.save(mentee);
            return true;
        }

        return false;
    }

}
