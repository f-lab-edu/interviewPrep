package com.example.interviewPrep.quiz.utils;

public enum MonthDuration {
    ONE(1), THREE(3), SIX(6), TWELVE(12);

    private int months;

    private MonthDuration(int months) {
        this.months = months;
    }

    public int getMonths() {
        return months;
    }
}
