package com.example.interviewPrep.quiz.member.service;

import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.member.dto.OAuthAttributes;
import com.example.interviewPrep.quiz.member.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest,OAuth2User> delegate = new DefaultOAuth2UserService(); //OAuth2UserService의 구현체
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //로그인 진행 중인 서비스 ex(구글, 카카오)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        // OAuth2 로그인 진행 시 키가 되는 필드 값 (== primary key와 같은의미) 구글 기본 코드 ="sub", 네이버, 카카오는 기본 지원x

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oauth2User.getAttributes());
        // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스

        Member user = saveOrUpdate(attributes, registrationId); //로그인 한 유저 정보
        httpSession.setAttribute("user", new SessionUser(user)); //세션에 사용자 정보를 저장하기 위한 Dto 클래스

        //로그인 한 유저를 리턴함
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())), attributes.getAttributes(), attributes.getNameAttributeKey());
                                    //단 한개의 객체만 저장 가능한 컬렉션을 만들기 위해 singleton 사용, Granted Authority 를 implement 한 클래스
    }

    private Member saveOrUpdate(OAuthAttributes attributes, String registrationId){ //이메일로 사용자 찾아 정보를 업뎃, 없으면 유저 등록
        Member user = memberRepository.findByEmailAndType(attributes.getEmail(), registrationId).map(entity->entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        user.setType(registrationId);
        return memberRepository.save(user);
    }
}
