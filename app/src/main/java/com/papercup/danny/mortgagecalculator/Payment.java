package com.papercup.danny.mortgagecalculator;


public class Payment {

    private double principal;
    private double interest;
    private double remainingBalance;
    private int month;

    public void setMonth(int month) {
        this.month = month;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public int getMonth() {
        return month;
    }

    public double getPrincipal() {
        return principal;
    }

    public double getInterest() {
        return interest;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }
}
