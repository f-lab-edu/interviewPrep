package com.example.interviewPrep.quiz.member.service;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.dto.request.MemberRequest;
import com.example.interviewPrep.quiz.member.dto.response.MemberResponse;
import com.example.interviewPrep.quiz.member.exception.LoginFailureException;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.redis.RedisDao;
import com.example.interviewPrep.quiz.utils.AES256;;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.DUPLICATE_EMAIL;
import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_MEMBER;
import static com.example.interviewPrep.quiz.member.dto.response.MemberResponse.createMemberResponse;

@Service
public class MemberService {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final RedisDao redisDao;

    public MemberService(JwtService jwtService, MemberRepository memberRepository, RedisDao redisDao) {
        this.jwtService = jwtService;
        this.memberRepository = memberRepository;
        this.redisDao = redisDao;
    }


    public void createMember(MemberRequest memberRequest) {

        AES256 aes256 = new AES256();
        String email = memberRequest.getEmail();

        if (isDuplicatedEmail(email)) {
            throw new CommonException(DUPLICATE_EMAIL);
        }

        Member member = MemberRequest.createMember(memberRequest);

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
        Long id = jwtService.getMemberId();
        Member member = memberRepository.findById(id).orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));
        member.setPassword(null);
        return createMemberResponse(member);
    }

    public MemberResponse updateNickNameAndEmail(MemberRequest memberRequest, Authentication authentication) {

        String memberId = authentication.getName();

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

        Long id = jwtService.getMemberId();

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
