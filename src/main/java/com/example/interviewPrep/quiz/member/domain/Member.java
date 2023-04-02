package com.example.interviewPrep.quiz.member.domain;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.domain.BaseTimeEntity;
import com.example.interviewPrep.quiz.member.dto.Role;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Entity
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name= "i_member", columnList = "email"))
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Member extends BaseTimeEntity {
    public Member(String email, String password, String nickName){
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.role = Role.USER;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MEMBER_ID")
    private Long id;

    @Column(nullable = false)
    private String email;

    private String password;

    private String type;

    private String nickName;

    @OneToMany(mappedBy = "member")
    @JsonManagedReference
    private List<Answer> answers = new ArrayList<>();
    private String name;
    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    private Role role;

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
