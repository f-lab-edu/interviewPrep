package com.example.interviewPrep.quiz.security;

import com.example.interviewPrep.quiz.member.service.CustomOAuth2UserService;
import com.example.interviewPrep.quiz.IndexController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
@SpringBootTest 어노테이션은 스프링 부트에서 관리하는 모든 빈들을 생성한 후에 테스트를 실행하기 때문에 테스트에 많은 시간이 소요되며, 단위 테스트에서는 적절하지 않다.
 @WebMvcTest 어노테이션은 웹과 관련된 빈들만 생성해줘서 비교적 가볍다.

 @MockBeans, @MockBean 을 통해 필요한 빈들을 생성한다.
@WebMvcTest 에서 테스트에 필요한 모든 빈들을 생성해 주지 않기 때문에 @MockBean 어노테이션을 통해 필요한 빈들을 목업 해줄 수 있다.

MockMvc 클래스를 통해 스프링 MVC의 동작을 재현할 수 있다.
 */
@WebMvcTest(IndexController.class) //테스트할 컨트롤러 지정
public class IndexWebControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomOAuth2UserService customOAuth2UserService;


    @Test
    @DisplayName(" / 처음 화면에서 로그인")
    void indexPage() throws Exception{

        mockMvc.perform(get("/")
                .with(oauth2Login()
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                .attributes(attributes ->{
                    attributes.put("username", "username");
                    attributes.put("name", "name");
                    attributes.put("email", "my@email");
                    attributes.put("picture", "https://my_picture");
                })
                ))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    @DisplayName("인증 하지 않고 다른 url 이동 시 로그인 화면으로 이동")
    void questionPage() throws Exception{

        mockMvc.perform(get("/question/java"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());

    }

}
