package com.example.interviewPrep.quiz.member.domain;

import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.member.dto.Role;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@Table(indexes = @Index(name = "i_member", columnList = "email"))
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String type;

    private String nickName;

    private String name;

    private String picture;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isPaid;

    @Builder
    public Member(Long id, String email, String password, String type, String nickName, String name, String picture, Role role, boolean isPaid) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.type = type;
        this.nickName = nickName;
        this.name = name;
        this.picture = picture;
        this.role = role;
        this.isPaid = isPaid;
    }

    public static Member createMemberEntity(String email, String password, String nickName) {
        return Member.builder()
                .email(email)
                .password(password)
                .nickName(nickName)
                .isPaid(false)
                .build();
    }

    public void setEmail(String email) {
        Objects.requireNonNull(email, "email이 없습니다.");
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(String type) {
        Objects.requireNonNull(type, "type이 없습니다.");
        this.type = type;
    }

    public void setNickName(String nickName) {
        Objects.requireNonNull(nickName, "nickName이 없습니다.");
        this.nickName = nickName;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Member update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    public void createPwd(String password) {
        this.password = password;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
