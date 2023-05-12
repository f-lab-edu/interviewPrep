package com.example.interviewPrep.quiz.member.controller;

import com.example.interviewPrep.quiz.member.dto.*;
import com.example.interviewPrep.quiz.member.service.AuthenticationService;
import com.example.interviewPrep.quiz.member.service.MemberService;
import com.example.interviewPrep.quiz.member.social.service.OauthService;
import com.example.interviewPrep.quiz.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/members/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class MemberController {
    private final AuthenticationService authService;
    private final MemberService memberService;
    private final OauthService oauthService;

    @PostMapping("signup")
    public ResultResponse<?> signUp(@RequestBody SignUpRequestDTO memberDTO) throws Exception {
        return ResultResponse.success(memberService.createMember(memberDTO));
    }

    @GetMapping("userInfo")
    public ResultResponse<?> getUserInfo(){
        return ResultResponse.success(memberService.getUserInfo());
    }

    @PostMapping("login")
    public ResultResponse<?> login(@RequestBody @NotNull LoginRequestDTO memberDTO, HttpServletResponse response){
        return ResultResponse.success(authService.login(memberDTO, response));
    }

    @PutMapping("/change")
    public ResultResponse<?> changeNickNameAndEmail(@RequestBody @NotNull MemberDTO memberDTO){
        return ResultResponse.success(memberService.changeNickNameAndEmail(memberDTO));
    }

    @PutMapping("/password/change")
    public ResultResponse<?> changePassword(@RequestBody @NotNull MemberDTO memberDTO){
        return ResultResponse.success(memberService.changePassword(memberDTO));
    }

    @GetMapping(value="auth/{socialType}")
    public void socialLoginType(@PathVariable String socialType){
        oauthService.request(socialType);
    }

    @GetMapping(value="auth/{socialType}/callback")
    public ResultResponse<?> callback(@PathVariable String socialType, @RequestParam(name="code") String code){
        return ResultResponse.success(oauthService.socialLogin(socialType, code));
    }

    @GetMapping(value="logout")
    public ResultResponse<?> logout(HttpServletRequest request){
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        authService.logout(accessToken);
        return ResultResponse.success(ResponseEntity.noContent().build());
    }


    @GetMapping(value="reissue")
    public ResultResponse<LoginResponseDTO> reissueToken(@CookieValue(value="refreshToken", defaultValue = "0" ) String cookie){
        return ResultResponse.success(authService.reissue(cookie));
    }

}
