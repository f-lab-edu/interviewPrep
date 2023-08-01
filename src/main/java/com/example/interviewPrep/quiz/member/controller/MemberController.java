package com.example.interviewPrep.quiz.member.controller;

import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.dto.*;
import com.example.interviewPrep.quiz.member.service.AuthenticationService;
import com.example.interviewPrep.quiz.member.service.MemberService;
import com.example.interviewPrep.quiz.member.social.service.OauthService;
import com.example.interviewPrep.quiz.response.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class MemberController {
    private final AuthenticationService authService;
    private final MemberService memberService;
    private final OauthService oauthService;

    public MemberController(AuthenticationService authService, MemberService memberService, OauthService oauthService){
        this.authService = authService;
        this.memberService = memberService;
        this.oauthService = oauthService;
    }

    @PostMapping("/api/v1/members/signup")
    public ResultResponse<Member> signUp(@RequestBody SignUpRequestDTO memberDTO) throws Exception {
        return ResultResponse.success(memberService.createMember(memberDTO));
    }

    @GetMapping("/api/v1/members/userInfo")
    public ResultResponse<Member> getUserInfo(){
        return ResultResponse.success(memberService.getUserInfo());
    }

    @PostMapping("/api/v1/members/login")
    public ResultResponse<LoginResponseDTO> login(@RequestBody @NotNull LoginRequestDTO memberDTO, HttpServletResponse response){
        return ResultResponse.success(authService.login(memberDTO, response));
    }

    @PutMapping("/api/v1/members/change")
    public ResultResponse<Member> updateNickNameAndEmail(@RequestBody @NotNull MemberDTO memberDTO, @AuthenticationPrincipal Authentication authentication){
        return ResultResponse.success(memberService.updateNickNameAndEmail(memberDTO, authentication));
    }

    @PutMapping("/api/v1/members/password/change")
    public ResultResponse<Member> updatePassword(@RequestBody @NotNull MemberDTO memberDTO){
        return ResultResponse.success(memberService.updatePassword(memberDTO));
    }

    @GetMapping("/api/v1/members/auth/{socialType}")
    public void socialLoginType(@PathVariable String socialType){
        oauthService.request(socialType);
    }

    @GetMapping("/api/v1/members/auth/{socialType}/callback")
    public ResultResponse<LoginResponseDTO> callback(@PathVariable String socialType, @RequestParam(name="code") String code){
        return ResultResponse.success(oauthService.socialLogin(socialType, code));
    }

    @GetMapping("/api/v1/members/logout")
    public ResultResponse<?> logout(HttpServletRequest request){
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        authService.logout(accessToken);
        return ResultResponse.success(ResponseEntity.noContent().build());
    }


    @GetMapping("/api/v1/members/reissue")
    public ResultResponse<LoginResponseDTO> reissueToken(@CookieValue(value="refreshToken", defaultValue = "0" ) String cookie){
        return ResultResponse.success(authService.reissue(cookie));
    }

}
