package com.example.interviewPrep.quiz.member.controller;

import com.example.interviewPrep.quiz.member.dto.request.LoginRequest;
import com.example.interviewPrep.quiz.member.dto.request.MemberRequest;
import com.example.interviewPrep.quiz.member.dto.response.LoginResponse;
import com.example.interviewPrep.quiz.member.dto.response.MemberResponse;
import com.example.interviewPrep.quiz.member.service.AuthenticationService;
import com.example.interviewPrep.quiz.member.service.MemberService;
import com.example.interviewPrep.quiz.member.social.service.OauthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/members")
@CrossOrigin(origins = "*")
@Slf4j
public class MemberController {
    private final AuthenticationService authService;
    private final MemberService memberService;
    private final OauthService oauthService;

    public MemberController(AuthenticationService authService, MemberService memberService, OauthService oauthService) {
        this.authService = authService;
        this.memberService = memberService;
        this.oauthService = oauthService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody MemberRequest memberRequest) {
        memberService.createMember(memberRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/userInfo")
    public ResponseEntity<MemberResponse> getUserInfo() {
        return ResponseEntity.ok(memberService.getUserInfo());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(loginRequest, response));
    }

    @PutMapping("/change")
    public ResponseEntity<MemberResponse> updateNickNameAndEmail(@RequestBody @NotNull MemberRequest memberRequest, @AuthenticationPrincipal Authentication authentication) {
        return ResponseEntity.ok(memberService.updateNickNameAndEmail(memberRequest, authentication));
    }

    @PutMapping("/password/change")
    public ResponseEntity<MemberResponse> updatePassword(@RequestBody MemberRequest memberRequest) {
        return ResponseEntity.ok(memberService.updatePassword(memberRequest));
    }

    @GetMapping("/auth/{socialType}")
    public void socialLoginType(@PathVariable String socialType) {
        oauthService.request(socialType);
    }

    @GetMapping("/auth/{socialType}/callback")
    public ResponseEntity<LoginResponse> callback(@PathVariable String socialType, @RequestParam(name = "code") String code) {
        return ResponseEntity.ok(oauthService.socialLogin(socialType, code));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        authService.logout(accessToken);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/reissue")
    public ResponseEntity<LoginResponse> reissueToken(@CookieValue(value = "refreshToken", defaultValue = "0") String cookie) {
        return ResponseEntity.ok(authService.reissue(cookie));
    }

}
