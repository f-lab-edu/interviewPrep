package com.example.interviewPrep.quiz.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormat {

    public static String customLocalDateTime(LocalDateTime localDateTime) {
       return localDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }
}
