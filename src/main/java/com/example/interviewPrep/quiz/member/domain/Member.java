package com.example.interviewPrep.quiz.member.domain;

import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.member.dto.Role;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;

@Setter
@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(indexes = @Index(name= "i_member", columnList = "email"))
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MEMBER_ID")
    private Long id;

    @Column(nullable = false)
    private String email;

    private String password;

    private String type;

    private String nickName;

    private String name;
    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member() {
    }


    public Member(String email, String password, String nickName){
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.role = Role.USER;
    }

    public Member(String email, String password, String type, String nickName, String name, String picture, Role role){
        this.email = email;
        this.password = password;
        this.type = type;
        this.nickName = nickName;
        this.name = name;
        this.role = role;
        this.picture = picture;
    }

    public Member update(String name, String picture){
        this.name = name;
        this.picture = picture;

        return this;
    }

    public void createPwd(String password){
        this.password = password;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

}
