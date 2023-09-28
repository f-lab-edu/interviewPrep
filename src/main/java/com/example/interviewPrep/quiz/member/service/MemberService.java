package com.example.interviewPrep.quiz.member.service;

import com.example.interviewPrep.quiz.company.domain.Company;
import com.example.interviewPrep.quiz.company.repository.CompanyRepository;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.dto.request.MemberRequest;
import com.example.interviewPrep.quiz.member.dto.response.MemberResponse;
import com.example.interviewPrep.quiz.member.exception.LoginFailureException;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.redis.RedisDao;
import com.example.interviewPrep.quiz.utils.AES256;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;
import static com.example.interviewPrep.quiz.member.dto.response.MemberResponse.createMemberResponse;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final RedisDao redisDao;

    public MemberService(MemberRepository memberRepository, CompanyRepository companyRepository, RedisDao redisDao) {
        this.memberRepository = memberRepository;
        this.companyRepository = companyRepository;
        this.redisDao = redisDao;
    }


    public void createMember(MemberRequest memberRequest) {

        String email = memberRequest.getEmail();
        String companyName = memberRequest.getCompanyName();

        Company company = companyRepository.findByName(companyName)
                .orElseThrow(() -> new CommonException(NOT_FOUND_COMPANY));

        if (isDuplicatedEmail(email)) {
            throw new CommonException(DUPLICATE_EMAIL);
        }

        Member member = MemberRequest.createMember(memberRequest, company);

        memberRepository.save(member);

    }

    public boolean isDuplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.isPresent();
    }

    public boolean isDuplicatedNickName(String nickName) {
        return memberRepository.existsByNickName(nickName);
    }

    public MemberResponse getUserInfo() {
        Long id = JwtUtil.getMemberId();
        Member member = memberRepository.findById(id).orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));
        member.setPassword(null);
        return createMemberResponse(member);
    }

    public MemberResponse updateNickNameAndEmail(MemberRequest memberRequest, Authentication authentication) {

        // Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // UserDetails userDetails = (UserDetails)principal;
        String memberId = authentication.getName();
        // Long memberId = Long.parseLong(userDetails.getUsername());

        Member member = memberRepository.findById(Long.parseLong(memberId)).get();

        String newNickName = memberRequest.getNickName();
        String newEmail = memberRequest.getEmail();

        updateNickName(member, newNickName);
        updateEmail(member, newEmail);

        memberRepository.save(member);

        return createMemberResponse(member);
    }

    public void updateNickName(Member member, String newNickName) {
        if (isDuplicatedNickName(newNickName)) {
            throw new CommonException(DUPLICATE_EMAIL);
        }
        member.setNickName(newNickName);
    }

    public void updateEmail(Member member, String newEmail) {
        if (isDuplicatedEmail(newEmail)) {
            throw new CommonException(DUPLICATE_EMAIL);
        }
        member.setEmail(newEmail);
    }


    public MemberResponse updatePassword(MemberRequest memberRequest) {

        Long id = JwtUtil.getMemberId();

        Member member = memberRepository.findById(id).get();
        String password = member.getPassword();

        String inputPassword = memberRequest.getPassword();
        String hashedInputPassword = SHA256Util.encryptSHA256(inputPassword);
        String newPassword = memberRequest.getNewPassword();
        String hashedNewPassword = SHA256Util.encryptSHA256(newPassword);

        if (!isValidPassword(member, password, hashedInputPassword, hashedNewPassword)) {
            throw new LoginFailureException(member.getEmail());
        }

        return createMemberResponse(member);
    }

    public boolean isValidPassword(Member member, String password, String hashedInputPassword, String hashedNewPassword) {
        if (password.equals(hashedInputPassword)) {
            member.setPassword(hashedNewPassword);
            memberRepository.save(member);
            return true;
        }

        return false;
    }

}
