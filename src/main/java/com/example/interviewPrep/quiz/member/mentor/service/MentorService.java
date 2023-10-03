package com.example.interviewPrep.quiz.member.mentor.service;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.member.mentor.domain.Mentor;
import com.example.interviewPrep.quiz.member.mentor.dto.request.MentorRequest;
import com.example.interviewPrep.quiz.member.mentor.dto.response.MentorResponse;
import com.example.interviewPrep.quiz.member.mentor.repository.MentorRepository;
import com.example.interviewPrep.quiz.redis.RedisDao;
import com.example.interviewPrep.quiz.utils.AES256;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;
import static com.example.interviewPrep.quiz.member.mentor.dto.response.MentorResponse.createMentorResponse;

@Service
public class MentorService {
    private final MentorRepository mentorRepository;
    private final RedisDao redisDao;

    public MentorService(MentorRepository mentorRepository, RedisDao redisDao) {
        this.mentorRepository = mentorRepository;
        this.redisDao = redisDao;
    }


    public void createMentor(MentorRequest mentorRequest) {

        AES256 aes256 = new AES256();
        String email = mentorRequest.getEmail();

        if (isDuplicatedEmail(email)) {
            throw new CommonException(DUPLICATE_EMAIL);
        }

        Mentor mentor = MentorRequest.createMentor(mentorRequest);
        mentorRepository.save(mentor);
    }

    public boolean isDuplicatedEmail(String email) {
        Optional<Mentor> mentor = mentorRepository.findByEmail(email);
        return mentor.isPresent();
    }

    public boolean isDuplicatedNickName(String nickName) {
        Optional<Mentor> mentor = mentorRepository.findByNickName(nickName);
        return mentor.isPresent();
    }

    public MentorResponse getUserInfo() {
        Long id = JwtUtil.getMemberId();
        Mentor mentor = mentorRepository.findById(id).orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));
        mentor.setPassword(null);
        return createMentorResponse(mentor);
    }

    public void updateNickName(Mentor mentor, String newNickName) {
        if (isDuplicatedNickName(newNickName)) {
            throw new CommonException(DUPLICATE_EMAIL);
        }
        mentor.setNickName(newNickName);
    }

    public void updateEmail(Mentor mentor, String newEmail) {
        if (isDuplicatedEmail(newEmail)) {
            throw new CommonException(DUPLICATE_EMAIL);
        }
        mentor.setEmail(newEmail);
    }


    public MentorResponse updatePassword(MentorRequest mentorRequest) {

        Long menteeId = JwtUtil.getMemberId();

        Mentor mentor = mentorRepository.findById(menteeId).orElseThrow(() -> new CommonException(NOT_FOUND_MENTEE));
        String password = mentor.getPassword();

        String inputPassword = mentorRequest.getPassword();
        String hashedInputPassword = SHA256Util.encryptSHA256(inputPassword);
        String newPassword = mentorRequest.getNewPassword();
        String hashedNewPassword = SHA256Util.encryptSHA256(newPassword);

        if (!isValidPassword(mentor, password, hashedInputPassword, hashedNewPassword)) {
            throw new CommonException(WRONG_PASSWORD);
        }

        return createMentorResponse(mentor);
    }

    public boolean isValidPassword(Mentor mentor, String password, String hashedInputPassword, String hashedNewPassword) {
        if (password.equals(hashedInputPassword)) {
            mentor.setPassword(hashedNewPassword);
            mentorRepository.save(mentor);
            return true;
        }

        return false;
    }

}

