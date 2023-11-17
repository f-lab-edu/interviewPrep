package com.example.interviewPrep.quiz.member.mentor.dto.request;

import com.example.interviewPrep.quiz.company.domain.Company;
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
    private final Long company_id;
    private final String picture;
    private String newPassword;

    @Builder
    public MentorRequest(String email, String password, String expertise, Long company_id, String careerLevel, String name, String nickName, String picture) {
        Objects.requireNonNull(email, "email이 null입니다.");
        Objects.requireNonNull(password, "password가 null입니다.");
        Objects.requireNonNull(expertise, "expertise가 null입니다.");
        Objects.requireNonNull(careerLevel, "careerLevel이 null입니다.");
        Objects.requireNonNull(name, "name이 null입니다.");
        Objects.requireNonNull(nickName, "nickName이 null입니다.");
        Objects.requireNonNull(company_id, "company_id가 null입니다.");
        Objects.requireNonNull(picture, "picture가 null입니다.");

        this.email = email;
        this.password = password;
        this.expertise = expertise;
        this.company_id = company_id;
        this.careerLevel = careerLevel;
        this.name = name;
        this.nickName = nickName;
        this.picture = picture;
    }

    public static Mentor createMentor(MentorRequest mentorRequest, Company company) {
        String password = SHA256Util.encryptSHA256(mentorRequest.getPassword());

        return Mentor.builder()
                .email(mentorRequest.getEmail())
                .password(password)
                .expertise(mentorRequest.getExpertise())
                .company(company)
                .careerLevel(mentorRequest.getCareerLevel())
                .name(mentorRequest.getName())
                .nickName(mentorRequest.getNickName())
                .picture(mentorRequest.getPicture())
                .build();
    }
}
