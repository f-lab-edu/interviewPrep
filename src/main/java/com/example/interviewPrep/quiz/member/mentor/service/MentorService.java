package com.example.interviewPrep.quiz.member.mentor.service;

import com.example.interviewPrep.quiz.company.domain.Company;
import com.example.interviewPrep.quiz.company.repository.CompanyRepository;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.jwt.service.JwtService;
import com.example.interviewPrep.quiz.member.mentor.domain.Mentor;
import com.example.interviewPrep.quiz.member.mentor.dto.request.DayAndTime;
import com.example.interviewPrep.quiz.member.mentor.dto.request.MentorRequest;
import com.example.interviewPrep.quiz.member.mentor.dto.response.MentorResponse;
import com.example.interviewPrep.quiz.member.mentor.repository.MentorRepository;
import com.example.interviewPrep.quiz.redis.RedisDao;
import com.example.interviewPrep.quiz.schedule.domain.WeeklySchedule;
import com.example.interviewPrep.quiz.schedule.repository.WeeklyScheduleRepository;
import com.example.interviewPrep.quiz.utils.AES256;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.DUPLICATE_EMAIL;
import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_MEMBER;
import static com.example.interviewPrep.quiz.member.mentor.dto.response.MentorResponse.createMentorResponse;

@Service
@Slf4j
@Transactional(readOnly = true)
public class MentorService {

    Logger logger = LoggerFactory.getLogger(MentorService.class);
    private final JwtService jwtService;

    private final WeeklyScheduleRepository weeklyScheduleRepository;
    private final CompanyRepository companyRepository;
    private final MentorRepository mentorRepository;
    private final RedisDao redisDao;

    public MentorService(JwtService jwtService, MentorRepository mentorRepository, WeeklyScheduleRepository weeklyScheduleRepository, CompanyRepository companyRepository, RedisDao redisDao) {
        this.jwtService = jwtService;
        this.mentorRepository = mentorRepository;
        this.weeklyScheduleRepository = weeklyScheduleRepository;
        this.companyRepository = companyRepository;
        this.redisDao = redisDao;
    }


    public WeeklySchedule getScheduleByDayOfTheWeek(String schedule) {

        ArrayList<Integer>[] dayOfTheWeek = new ArrayList[7];

        for(int i=0; i<7; i++){
            dayOfTheWeek[i] = new ArrayList<>();
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            DayAndTime[] scheduleArray = objectMapper.readValue(schedule, DayAndTime[].class);

            for (DayAndTime obj : scheduleArray) {
                dayOfTheWeek[obj.getDay()].add(obj.getTime()+9);
            }

        } catch (Exception e) {
            logger.error("에러입니다.", e);
        }


        List<String> weeklyTimes = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            ArrayList<Integer> tmpList = dayOfTheWeek[i];
            Collections.sort(tmpList);
            StringBuilder time = new StringBuilder();
            for (int j = 0; j < tmpList.size(); j++) {
                if(j != tmpList.size()-1){
                    time.append(Integer.toString(tmpList.get(j))).append(" ");
                }
                time.append(Integer.toString(tmpList.get(j)));
            }
            weeklyTimes.add(time.toString());
        }

        String Monday = weeklyTimes.get(0);
        String Tuesday = weeklyTimes.get(1);
        String Wednesday = weeklyTimes.get(2);
        String Thursday = weeklyTimes.get(3);
        String Friday = weeklyTimes.get(4);
        String Saturday = weeklyTimes.get(5);
        String Sunday = weeklyTimes.get(6);

        WeeklySchedule weeklySchedule = new WeeklySchedule(Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday);
        return weeklyScheduleRepository.save(weeklySchedule);
    }

    @Transactional
    public void createMentor(MentorRequest mentorRequest) {

        Company company = companyRepository.findByName(mentorRequest.getCompanyName()).orElseThrow(null);

        WeeklySchedule weeklySchedule = getScheduleByDayOfTheWeek(mentorRequest.getSchedule());

        AES256 aes256 = new AES256();
        String email = mentorRequest.getEmail();

        if (isDuplicatedEmail(email)) {
            throw new CommonException(DUPLICATE_EMAIL);
        }

        Mentor mentor = MentorRequest.createMentor(mentorRequest, company, weeklySchedule);
        mentorRepository.save(mentor);
    }


    public List<Mentor> getMentors() {
        List<Mentor> mentors = mentorRepository.findAll();

        if(mentors.isEmpty()){
            throw new RuntimeException("Mentors list is empty");
        }
        return mentors;
    }


    public boolean isDuplicatedEmail(String email) {
        Optional<Mentor> mentor = mentorRepository.findByEmail(email);
        return mentor.isPresent();
    }

    public boolean isDuplicatedNickName(String nickName) {
        Optional<Mentor> mentor = mentorRepository.findByNickName(nickName);
        return mentor.isPresent();
    }

    public MentorResponse getUserInfo() {
        Long id = JwtService.getMemberId();
        Mentor mentor = mentorRepository.findById(id).orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));
        mentor.setPassword(null);
        return createMentorResponse(mentor);
    }

    public void updateNickName(Mentor mentor, String newNickName) {
        if (isDuplicatedNickName(newNickName)) {
            throw new CommonException(DUPLICATE_EMAIL);
        }
        mentor.setNickName(newNickName);
    }

    public void updateEmail(Mentor mentor, String newEmail) {
        if (isDuplicatedEmail(newEmail)) {
            throw new CommonException(DUPLICATE_EMAIL);
        }
        mentor.setEmail(newEmail);
    }


    /*
    public MentorResponse updatePassword(MentorRequest mentorRequest) {

        Long menteeId = JwtService.getMemberId();

        Mentor mentor = mentorRepository.findById(menteeId).orElseThrow(() -> new CommonException(NOT_FOUND_MENTEE));
        String password = mentor.getPassword();

        String inputPassword = mentorRequest.getPassword();
        String hashedInputPassword = SHA256Util.encryptSHA256(inputPassword);
        String newPassword = mentorRequest.getNewPassword();
        String hashedNewPassword = SHA256Util.encryptSHA256(newPassword);

        if (!isValidPassword(mentor, password, hashedInputPassword, hashedNewPassword)) {
            throw new CommonException(WRONG_PASSWORD);
        }

        return createMentorResponse(mentor);
    }

    public boolean isValidPassword(Mentor mentor, String password, String hashedInputPassword, String hashedNewPassword) {
        if (password.equals(hashedInputPassword)) {
            mentor.setPassword(hashedNewPassword);
            mentorRepository.save(mentor);
            return true;
        }

        return false;
    }
    */
}

