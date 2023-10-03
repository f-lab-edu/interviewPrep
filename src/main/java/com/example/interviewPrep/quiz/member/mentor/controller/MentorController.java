package com.example.interviewPrep.quiz.member.mentor.controller;

import com.example.interviewPrep.quiz.member.dto.request.LoginRequest;
import com.example.interviewPrep.quiz.member.dto.response.LoginResponse;
import com.example.interviewPrep.quiz.member.mentor.dto.request.MentorRequest;
import com.example.interviewPrep.quiz.member.mentor.dto.response.MentorResponse;
import com.example.interviewPrep.quiz.member.mentor.service.MentorService;
import com.example.interviewPrep.quiz.member.service.AuthenticationService;
import com.example.interviewPrep.quiz.member.social.service.OauthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/members/mentor")
@CrossOrigin(origins = "*")
@Slf4j
public class MentorController {
    private final AuthenticationService authService;
    private final MentorService mentorService;
    private final OauthService oauthService;

    public MentorController(AuthenticationService authService, MentorService mentorService, OauthService oauthService) {
        this.authService = authService;
        this.mentorService = mentorService;
        this.oauthService = oauthService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody MentorRequest mentorRequest) {
        mentorService.createMentor(mentorRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/userInfo")
    public ResponseEntity<MentorResponse> getUserInfo() {
        return ResponseEntity.ok(mentorService.getUserInfo());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(loginRequest, response));
    }

    @PutMapping("/password/change")
    public ResponseEntity<MentorResponse> updatePassword(@RequestBody MentorRequest mentorRequest) {
        return ResponseEntity.ok(mentorService.updatePassword(mentorRequest));
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
