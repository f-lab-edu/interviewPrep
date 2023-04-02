package com.example.interviewPrep.quiz.member.service;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.member.dto.MemberDTO;
import com.example.interviewPrep.quiz.member.exception.LoginFailureException;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.dto.SignUpRequestDTO;
import com.example.interviewPrep.quiz.redis.RedisDao;
import com.example.interviewPrep.quiz.utils.AES256;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;


    @Autowired
    private final RedisDao redisDao;


    public Member createMember(SignUpRequestDTO memberDTO) throws Exception {

            AES256 aes256 = new AES256();
            String code = memberDTO.getCode();
            String email = redisDao.getValues(aes256.decrypt(code));

            String password = SHA256Util.encryptSHA256(memberDTO.getPassword());

            boolean duplicatedEmail = isDuplicatedEmail(email);
            if(duplicatedEmail){
                throw new CommonException(DUPLICATE_EMAIL);
            }

            Member member = new Member(email, password, memberDTO.getNickName());
            memberRepository.save(member);
            return member;
    }

    public boolean isDuplicatedEmail(String email){

            Optional<Member> member = memberRepository.findByEmail(email);

            if(member.isPresent()){
                return true;
            }
            return false;
    }


    public Member getUserInfo(){

            Long id = JwtUtil.getMemberId();
            Member member = memberRepository.findById(id).orElseThrow();
            member.setPassword(null);
            return member;

    }
    public Member changeNickNameAndEmail(MemberDTO memberDTO){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;

        Long memberId = Long.parseLong(userDetails.getUsername());

        Member member = memberRepository.findById(memberId).get();

        String newNickName = memberDTO.getNickName();
        String newEmail = memberDTO.getEmail();

        if(newNickName != null){
            boolean duplicateNickName = memberRepository.existsByNickName(newNickName);
            if(duplicateNickName){
                throw new CommonException(DUPLICATE_NICKNAME);
            }
            member.setNickName(newNickName);
        }

        if(newEmail != null){
            boolean duplicateEmail = memberRepository.existsByEmail(newEmail);
            if(duplicateEmail){
                throw new CommonException(DUPLICATE_EMAIL);
            }
            member.setEmail(newEmail);
        }

        memberRepository.save(member);

        return member;
    }


    public Member changeEmail(MemberDTO memberDTO){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;

        Long memberId = Long.parseLong(userDetails.getUsername());

        Member member = memberRepository.findById(memberId).get();

        String newEmail = memberDTO.getEmail();
        member.setEmail(newEmail);
        memberRepository.save(member);

        return member;
    }


    public Member changePassword(MemberDTO memberDTO){

        Long id = JwtUtil.getMemberId();

        Member member = memberRepository.findById(id).get();

        String password = memberDTO.getPassword();

        String newPassword = memberDTO.getNewPassword();

        String encryptedPassword = SHA256Util.encryptSHA256(password);

        if(member.getPassword().equals(encryptedPassword)){
            String newEncryptedPassword = SHA256Util.encryptSHA256(newPassword);
            member.setPassword(newEncryptedPassword);
            memberRepository.save(member);
            return member;
        }else{
            throw new LoginFailureException(member.getEmail());
        }

    }

}
