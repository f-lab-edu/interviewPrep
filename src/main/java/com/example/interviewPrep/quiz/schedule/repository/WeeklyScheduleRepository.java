package com.example.interviewPrep.quiz.schedule.repository;

import com.example.interviewPrep.quiz.schedule.domain.WeeklySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyScheduleRepository extends JpaRepository<WeeklySchedule, Long> {
    WeeklySchedule save(WeeklySchedule weeklySchedule);
}
