package com.example.interviewPrep.quiz.config;

import com.example.interviewPrep.quiz.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
//                .headers().frameOptions().disable() // H2 데이터 베이스 콘솔에 접근
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .authorizeRequests() //url별 권한 접근제어 관리 옵션 시작점
                .antMatchers("/", "/members/auth"/*, "/css/**", "/images/**", "/js/**", "/h2-console/**"*/).permitAll() //권한 관리 대상 지정
                .antMatchers( "/answer/**").authenticated()
                .antMatchers(HttpMethod.POST, "/question/ref/**").authenticated()
                .antMatchers(HttpMethod.POST, "/subscribe").permitAll()
                //.antMatchers("/members/userInfo", "/members/change", "/members/password/change").authenticated()
                //.anyRequest().authenticated() //나머지 요청들은 인증된 사람에게만 공개( 인증된 사람 == 로그인 사용자)
                .anyRequest().permitAll()
                .and()
                .logout()
                .logoutUrl("/server/logout")
                .logoutSuccessUrl("/")// //로그아웃 성공시 이동
                .deleteCookies("refreshToken")
                .clearAuthentication(true);//로그아웃 시 Authentication 삭제

    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
