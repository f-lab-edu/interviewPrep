package com.example.interviewPrep.quiz.member.mentee.domain;

import com.example.interviewPrep.quiz.company.domain.Company;
import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.member.mentor.domain.Mentor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@Table(indexes = @Index(name = "i_mentee", columnList = "email"))
public class Mentee extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENTEE_ID")
    private Long id;

    private String name;

    private String nickName;

    private String email;

    private String password;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MENTOR_ID")
    private Mentor mentor;

    private String picture;

    private boolean isPaid;

    private String type;

    @Builder
    public Mentee(String name, String nickName, String email, String password, Mentor mentor, String picture, boolean isPaid, String type, Company company) {
        Objects.requireNonNull(name, "name이 null입니다.");
        Objects.requireNonNull(nickName, "nickName이 null입니다.");
        Objects.requireNonNull(email, "email이 null입니다.");
        Objects.requireNonNull(password, "password가 null입니다.");
        Objects.requireNonNull(type, "type이 null입니다.");
        Objects.requireNonNull(company, "company가 null입니다.");

        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
        this.mentor = mentor;
        this.picture = picture;
        this.isPaid = isPaid;
        this.type = type;
        this.company = company;
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

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Mentee update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    public void createPwd(String password) {
        this.password = password;
    }

}
