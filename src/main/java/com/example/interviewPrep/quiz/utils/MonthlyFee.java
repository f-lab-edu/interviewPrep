package com.example.interviewPrep.quiz.utils;

public enum MonthlyFee {

    ONE(9900), THREE(8900), SIX(7900), TWELVE(6900);

    private final int cost;

    private MonthlyFee(int cost) {
        this.cost = cost;
    }

    public int getCosts() {
        return cost;
    }

}
