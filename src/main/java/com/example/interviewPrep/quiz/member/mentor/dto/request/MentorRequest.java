package com.example.interviewPrep.quiz.member.mentor.dto.request;

import com.example.interviewPrep.quiz.member.domain.Mentee;
import com.example.interviewPrep.quiz.member.mentee.dto.request.MenteeRequest;
import com.example.interviewPrep.quiz.member.mentor.domain.Mentor;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class MentorRequest {

    private final String email;
    private final String password;
    private final String expertise;
    private final String careerLevel;
    private final String name;
    private final String nickName;
    private String newPassword;

    @Builder
    public MentorRequest(String email, String password, String expertise, String careerLevel, String name, String nickName) {
        Objects.requireNonNull(email, "email이 null입니다.");
        Objects.requireNonNull(password, "password가 null입니다.");
        Objects.requireNonNull(expertise, "expertise가 null입니다.");
        Objects.requireNonNull(careerLevel, "careerLevel이 null입니다.");
        Objects.requireNonNull(name, "name이 null입니다.");
        Objects.requireNonNull(nickName, "nickName이 null입니다.");

        this.email = email;
        this.password = password;
        this.expertise = expertise;
        this.careerLevel = careerLevel;
        this.name = name;
        this.nickName = nickName;
    }

    public static Mentor createMentor(MentorRequest mentorRequest) {
        String password = SHA256Util.encryptSHA256(mentorRequest.getPassword());

        return Mentor.builder()
                .email(mentorRequest.getEmail())
                .password(password)
                .expertise(mentorRequest.getExpertise())
                .careerLevel(mentorRequest.getCareerLevel())
                .name(mentorRequest.getName())
                .nickName(mentorRequest.getNickName())
                .build();
    }
}
