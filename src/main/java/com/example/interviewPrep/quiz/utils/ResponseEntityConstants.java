package com.example.interviewPrep.quiz.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityConstants {

    public static final ResponseEntity<Void> RESPONSE_OK = new ResponseEntity(HttpStatus.OK);
    public static final ResponseEntity<Void> RESPONSE_CREATED= new ResponseEntity(HttpStatus.CREATED);
    public static final ResponseEntity<Void> RESPONSE_CONFLICT = new ResponseEntity(HttpStatus.CONFLICT);
    public static final ResponseEntity<Void> RESPONSE_NOT_FOUND = new ResponseEntity(HttpStatus.NOT_FOUND);
    public static final ResponseEntity<Void> RESPONSE_SERVER_ERROR = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    public static final ResponseEntity<Void> RESPONSE_NO_CONTENT = new ResponseEntity(HttpStatus.NO_CONTENT);
}
