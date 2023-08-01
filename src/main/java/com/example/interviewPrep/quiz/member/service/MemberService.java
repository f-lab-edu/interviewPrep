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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final RedisDao redisDao;

    public MemberService(MemberRepository memberRepository, RedisDao redisDao){
      this.memberRepository = memberRepository;
      this.redisDao = redisDao;
    }


    public Member createMember(SignUpRequestDTO memberDTO){

            AES256 aes256 = new AES256();
            // String code = memberDTO.getCode();
            // String email = redisDao.getValues(aes256.decrypt(code));
            String email = memberDTO.getEmail();

            String password = SHA256Util.encryptSHA256(memberDTO.getPassword());

            if(isDuplicatedEmail(email)){
                throw new CommonException(DUPLICATE_EMAIL);
            }

            Member member = Member.builder()
                            .email(email)
                            .password(password)
                            .nickName(memberDTO.getNickName())
                            .build();

            memberRepository.save(member);
            return member;
    }

    public boolean isDuplicatedEmail(String email){
        return memberRepository.existsByEmail(email);
    }

    public boolean isDuplicatedNickName(String nickName){
        return memberRepository.existsByNickName(nickName);
    }



    public Member getUserInfo(){
        Long id = JwtUtil.getMemberId();
        Member member = memberRepository.findById(id).orElseThrow();
        member.setPassword(null);
        return member;
    }

      public Member updateNickNameAndEmail(MemberDTO memberDTO, Authentication authentication){

          // Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
          // UserDetails userDetails = (UserDetails)principal;
          String memberId = authentication.getName();
          // Long memberId = Long.parseLong(userDetails.getUsername());

          Member member = memberRepository.findById(Long.parseLong(memberId)).get();

          String newNickName = memberDTO.getNickName();
          String newEmail = memberDTO.getEmail();

          updateNickName(member, newNickName);
          updateEmail(member, newEmail);

          memberRepository.save(member);

          return member;
      }

    public void updateNickName(Member member, String newNickName){
        if(isDuplicatedNickName(newNickName)){
          throw new CommonException(DUPLICATE_EMAIL);
        }
        member.setNickName(newNickName);
    }

    public void updateEmail(Member member, String newEmail){
      if(isDuplicatedEmail(newEmail)){
        throw new CommonException(DUPLICATE_EMAIL);
      }
      member.setEmail(newEmail);
    }


    public Member updatePassword(MemberDTO memberDTO){

        Long id = JwtUtil.getMemberId();

        Member member = memberRepository.findById(id).get();
        String password = member.getPassword();

        String inputPassword = memberDTO.getPassword();
        String hashedInputPassword = SHA256Util.encryptSHA256(inputPassword);
        String newPassword = memberDTO.getNewPassword();
        String hashedNewPassword = SHA256Util.encryptSHA256(newPassword);

        if(!isValidPassword(member, password, hashedInputPassword, hashedNewPassword)){
           throw new LoginFailureException(member.getEmail());
        }

        return member;
    }

    public boolean isValidPassword(Member member, String password, String hashedInputPassword, String hashedNewPassword){
      if(password.equals(hashedInputPassword)){
        member.setPassword(hashedNewPassword);
        memberRepository.save(member);
        return true;
      }

      return false;
    }

}
