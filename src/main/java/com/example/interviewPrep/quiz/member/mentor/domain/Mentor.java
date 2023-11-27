package com.example.interviewPrep.quiz.member.mentor.domain;

import com.example.interviewPrep.quiz.company.domain.Company;
import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.schedule.domain.WeeklySchedule;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@Table(indexes = @Index(name = "i_mentor", columnList = "email"))
public class Mentor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENTOR_ID")
    private Long id;

    private String email;

    private String password;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    private String expertise;

    private String careerLevel;

    private String nickName;

    private String name;

    private String picture;

    @OneToOne
    @JoinColumn(name = "WEEKLYSCHEDULE_ID")
    private WeeklySchedule weeklySchedule;

    @Builder
    public Mentor(String email, String password, String expertise, Company company, String careerLevel, String nickName, String name, String picture, WeeklySchedule weeklySchedule) {
        this.email = email;
        this.password = password;
        this.expertise = expertise;
        this.company = company;
        this.careerLevel = careerLevel;
        this.nickName = nickName;
        this.name = name;
        this.picture = picture;
        this.weeklySchedule = weeklySchedule;
    }


    public void setEmail(String email) {
        Objects.requireNonNull(email, "email이 없습니다.");
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setNickName(String nickName) {
        Objects.requireNonNull(nickName, "nickName이 없습니다.");
        this.nickName = nickName;
    }

}
