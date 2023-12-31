package com.example.interviewPrep.quiz.member.mentor.dto.request;

import com.example.interviewPrep.quiz.company.domain.Company;
import com.example.interviewPrep.quiz.member.mentor.domain.Mentor;
import com.example.interviewPrep.quiz.schedule.domain.WeeklySchedule;
import com.example.interviewPrep.quiz.utils.SHA256Util;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class MentorRequest {

    private final String name;
    private final String nickName;
    private final String email;
    private final String companyName;
    private final String expertise;
    private final String careerLevel;
    private final String password;
    private final String picture;
    private final String interviewTypes;
    private final String interviewLevels;
    private final String schedule;

    @Builder
    public MentorRequest(String name, String nickName, String email, String companyName, String expertise, String careerLevel, String password, String interviewLevels, String interviewTypes, String schedule, String picture) {
        Objects.requireNonNull(name, "name이 null입니다.");
        Objects.requireNonNull(nickName, "nickName이 null입니다.");
        Objects.requireNonNull(email, "email이 null입니다.");
        Objects.requireNonNull(companyName, "companyName이 null입니다.");
        Objects.requireNonNull(expertise, "expertise가 null입니다.");
        Objects.requireNonNull(careerLevel, "careerLevel이 null입니다.");
        Objects.requireNonNull(password, "password가 null입니다.");
        Objects.requireNonNull(interviewLevels, "interviewLevels가 null입니다.");
        Objects.requireNonNull(interviewTypes, "interviewTypes가 null입니다.");
        Objects.requireNonNull(schedule, "schedule이 null입니다.");

        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.companyName = companyName;
        this.expertise = expertise;
        this.careerLevel = careerLevel;
        this.password = password;
        this.interviewLevels = interviewLevels;
        this.interviewTypes = interviewTypes;
        this.schedule = schedule;
        this.picture = picture;
    }

    public static Mentor createMentor(MentorRequest mentorRequest, Company company, WeeklySchedule weeklySchedule) {
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
                .weeklySchedule(weeklySchedule)
                .interviewLevels(mentorRequest.getInterviewLevels())
                .interviewTypes(mentorRequest.getInterviewTypes())
                .build();
    }
}
