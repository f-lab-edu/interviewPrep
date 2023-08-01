package com.example.interviewPrep.quiz.member.domain;

import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.member.dto.Role;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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


    public void setEmail(String email){
        if(email == null){
            throw new NullPointerException("email이 없습니다");
        }
        this.email = email;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setType(String type){
        if(type == null){
            throw new NullPointerException("type이 없습니다");
        }
        this.type = type;
    }
    public void setNickName(String nickName){
        if(nickName == null){
            throw new NullPointerException("nickName이 없습니다");
        }
        this.nickName = nickName;
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
