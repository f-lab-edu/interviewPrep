package com.example.interviewPrep.quiz.email.service;

import java.time.Duration;
import java.util.Random;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.member.service.MemberService;
import com.example.interviewPrep.quiz.redis.RedisDao;
import com.example.interviewPrep.quiz.utils.AES256;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.DUPLICATE_EMAIL;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Autowired
    JavaMailSender emailSender;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    private final RedisDao redisDao;


    public static final String ePw = createKey();

    private MimeMessage createMessage(String to, String type)throws Exception{
        System.out.println("보내는 대상 : "+ to);
        System.out.println("인증 번호 : "+ePw);

        boolean duplicatedEmail = memberService.isDuplicatedEmail(to);
        System.out.println("duplicatedEmail 결과는?" + duplicatedEmail);

        if(duplicatedEmail){
            throw new CommonException(DUPLICATE_EMAIL);
        }

        AES256 aes256 = new AES256();
        String cipherePw = aes256.encrypt(ePw);
        System.out.println(cipherePw);
        System.out.println(aes256.decrypt(cipherePw));

        String emailVerifyUrl = "http://localhost:3000/signup/additional-info?code=" + cipherePw;

        redisDao.setValues(ePw, to, Duration.ofMinutes(10));


        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);//보내는 대상

        if(type == "change") {
            message.setSubject("Interviewprep 이메일 변경 인증");//제목
        }else if(type == "join"){
            message.setSubject("Interviewprep 회원가입 인증");//제목
        }

        String msgg="";
        msgg+= "<div style='margin:100px;'>";
        msgg+= "<h1> 안녕하세요 Interviewprep입니다. </h1>";
        msgg+= "<br>";
        msgg+= "<p>InterviewPrep을 이용하시기 위해 아래 링크를 클릭해주세요.<p>";
        msgg+= "<br>";
        msgg+= "<p>감사합니다!<p>";
        msgg+= "<br>";
        msgg+= "<a style='background-color: #4CAF50; border: none; color: white; padding: 15px 32px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px; cursor: pointer;' href='" + emailVerifyUrl + "'>InterviewPrep 인증하기</a>";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("acejongbok@gmail.com","InterviewPrep"));//보내는 사람

        return message;
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }

        return key.toString();
    }
    @Override
    public String sendSimpleMessage(String to, String type)throws Exception {
        // TODO Auto-generated method stub

        MimeMessage message = createMessage(to, type);

        try{//예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw;
    }

}