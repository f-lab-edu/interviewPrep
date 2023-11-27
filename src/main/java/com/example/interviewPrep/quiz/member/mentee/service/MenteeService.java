package com.example.interviewPrep.quiz.member.mentee.service;

import com.example.interviewPrep.quiz.company.domain.Company;
import com.example.interviewPrep.quiz.company.repository.CompanyRepository;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.member.mentee.domain.Mentee;
import com.example.interviewPrep.quiz.member.mentee.dto.request.MenteeRequest;
import com.example.interviewPrep.quiz.member.mentee.dto.response.MenteeResponse;
import com.example.interviewPrep.quiz.member.mentee.repository.MenteeRepository;
import com.example.interviewPrep.quiz.redis.RedisDao;
import com.example.interviewPrep.quiz.utils.AES256;;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;
import static com.example.interviewPrep.quiz.member.mentee.dto.response.MenteeResponse.createMenteeResponse;

@Service
@Transactional(readOnly = true)
public class MenteeService {

    private final JwtService jwtService;
    private final CompanyRepository companyRepository;
    private final MenteeRepository menteeRepository;
    private final RedisDao redisDao;

    public MenteeService(JwtService jwtService, CompanyRepository companyRepository, MenteeRepository menteeRepository, RedisDao redisDao) {
        this.jwtService = jwtService;
        this.companyRepository = companyRepository;
        this.menteeRepository = menteeRepository;
        this.redisDao = redisDao;
    }

    @Transactional
    public void createMentee(MenteeRequest menteeRequest) {

        Company company = companyRepository.findByName(menteeRequest.getCompanyName()).orElseThrow(null);

        AES256 aes256 = new AES256();
        String email = menteeRequest.getEmail();

        if (isDuplicatedEmail(email)) {
            throw new CommonException(DUPLICATE_EMAIL);
        }

        Mentee mentee = MenteeRequest.createMentee(menteeRequest, company);
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

    public MenteeResponse updatePassword(MenteeRequest menteeRequest) {

        Long menteeId = JwtService.getMemberId();

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
