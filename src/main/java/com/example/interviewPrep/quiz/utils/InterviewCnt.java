package com.example.interviewPrep.quiz.utils;

public enum InterviewCnt {

    ONE(1), THREE(3), SIX(6);

    private final int cnt;

    private InterviewCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getCnts() {
        return cnt;
    }
}
