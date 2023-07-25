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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final RedisDao redisDao;

    @Autowired
    public MemberService(MemberRepository memberRepository, RedisDao redisDao){
      this.memberRepository = memberRepository;
      this.redisDao = redisDao;
    }


    public Member createMember(SignUpRequestDTO memberDTO) throws Exception {

            AES256 aes256 = new AES256();
            // String code = memberDTO.getCode();
            // String email = redisDao.getValues(aes256.decrypt(code));
            String email = memberDTO.getEmail();

            String password = SHA256Util.encryptSHA256(memberDTO.getPassword());

            if(isDuplicatedEmail(email)){
                throw new CommonException(DUPLICATE_EMAIL);
            }

            Member member = new Member(email, password, memberDTO.getNickName());
            memberRepository.save(member);
            return member;
    }

    public boolean isDuplicatedEmail(String email){
        return memberRepository.findByEmail(email).isPresent();
    }


    public Member getUserInfo(){
            Long id = JwtUtil.getMemberId();
            Member member = memberRepository.findById(id).orElseThrow();
            member.setPassword(null);
            return member;
    }

      public Member updateNickNameAndEmail(MemberDTO memberDTO){

          Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
          UserDetails userDetails = (UserDetails)principal;

          Long memberId = Long.parseLong(userDetails.getUsername());

          Member member = memberRepository.findById(memberId).get();

          String newNickName = memberDTO.getNickName();
          String newEmail = memberDTO.getEmail();

          updateNickName(member, newNickName);
          updateEmail(member, newEmail);

          memberRepository.save(member);

          return member;
      }

    public void updateNickName(Member member, String newNickName){
        if(newNickName != null){
          if(memberRepository.existsByNickName(newNickName)){
            throw new CommonException(DUPLICATE_NICKNAME);
          }
          member.setNickName(newNickName);
        }
    }

    public void updateEmail(Member member, String newEmail){
      if(newEmail != null){
        boolean duplicateEmail = memberRepository.existsByEmail(newEmail);
        if(duplicateEmail){
          throw new CommonException(DUPLICATE_EMAIL);
        }
        member.setEmail(newEmail);
      }
    }


    public Member updatePassword(MemberDTO memberDTO){

        Long id = JwtUtil.getMemberId();

        Member member = memberRepository.findById(id).get();

        String inputPassword = memberDTO.getPassword();
        String newPassword = memberDTO.getNewPassword();

        if(!isValidPassword(member, member.getPassword(), inputPassword, newPassword)){
           throw new LoginFailureException(member.getEmail());
        }

        return member;
    }

    public boolean isValidPassword(Member member, String memberPassword, String inputPassword, String newPassword){

      String encryptedInputPassword = SHA256Util.encryptSHA256(inputPassword);

      if(memberPassword.equals(encryptedInputPassword)){
        String newEncryptedPassword = SHA256Util.encryptSHA256(newPassword);
        member.setPassword(newEncryptedPassword);
        memberRepository.save(member);
        return true;
      }

      return false;
    }

}
