package com.example.interviewPrep.quiz.config;

import com.example.interviewPrep.quiz.exception.advice.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                             AuthenticationException authException) throws IOException, ServletException {
                ErrorCode  errorCode = (ErrorCode) request.getAttribute("exception");

                //토큰 없는 경우
                if(errorCode == null) {
                        setResponse(response, NON_LOGIN);
                }

                // 토큰 만료된 경우
                else if(errorCode.equals(EXPIRED_TOKEN)) {
                        setResponseReissue(response, errorCode);
                }

                // 토큰 시그니처가 다른 경우
                else if(errorCode.equals(INVALID_TOKEN)) {
                        setResponse(response, errorCode);
                }

                else if(errorCode.equals(WRONG_TYPE_SIGNATURE)) {
                        setResponse(response, errorCode);
                }

                else if(errorCode.equals(WRONG_TOKEN)) {
                        setResponse(response, errorCode);
                }

                else if(errorCode.equals(WRONG_ID_TOKEN)) {
                        setResponse(response, errorCode);
                }
        }

         // 한글 출력을 위해 getWriter() 사용
        private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println("{ \"message\" : \"" + errorCode.getMessage()
                        + "\", \"code\" : \"" +  errorCode.getCode()
                        + ", \"errors\" : [ ] }");
        }


        private void setResponseReissue(HttpServletResponse response, ErrorCode errorCode) throws IOException {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION);
                response.getWriter().println("{ \"message\" : \"" + errorCode.getMessage()
                        + "\", \"code\" : \"" +  errorCode.getCode()
                        + ", \"errors\" : [ ] }");
        }


}
