package com.example.selection;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class FunctionLocationSpecificDiscount {
    private int discountAmount;
    private double discountRate;
    private int cashbackAmount;
    private double cashbackRate;
    private boolean ifLimitDayOfWeek;
    private boolean ifLimitTime;
    private ArrayList<String> validDayOfWeek;
    private ArrayList<String> validTime;
    private String details;

    public FunctionLocationSpecificDiscount(){}

    public FunctionLocationSpecificDiscount(
            int discountAmount, double discountRate,
            int cashbackAmount, double cashbackRate,
            boolean ifLimitDayOfWeek, boolean ifLimitTime,
            ArrayList<String> validDayOfWeek, ArrayList<String> validTime,
            String details) {
        this.discountAmount = discountAmount;
        this.discountRate = discountRate;
        this.cashbackAmount = cashbackAmount;
        this.cashbackRate = cashbackRate;
        this.ifLimitDayOfWeek = ifLimitDayOfWeek;
        this.ifLimitTime = ifLimitTime;
        this.validDayOfWeek = validDayOfWeek;
        this.validTime = validTime;
        this.details = details;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public int getCashbackAmount() {
        return cashbackAmount;
    }

    public void setCashbackAmount(int cashbackAmount) {
        this.cashbackAmount = cashbackAmount;
    }

    public double getCashbackRate() {
        return cashbackRate;
    }

    public void setCashbackRate(double cashbackRate) {
        this.cashbackRate = cashbackRate;
    }

    public boolean isIfLimitDayOfWeek() {
        return ifLimitDayOfWeek;
    }

    public void setIfLimitDayOfWeek(boolean ifLimitDayOfWeek) {
        this.ifLimitDayOfWeek = ifLimitDayOfWeek;
    }

    public boolean isIfLimitTime() {
        return ifLimitTime;
    }

    public void setIfLimitTime(boolean ifLimitTime) {
        this.ifLimitTime = ifLimitTime;
    }

    public ArrayList<String> getValidDayOfWeek() {
        return validDayOfWeek;
    }

    public void setValidDayOfWeek(ArrayList<String> validDayOfWeek) {
        this.validDayOfWeek = validDayOfWeek;
    }

    public ArrayList<String> getValidTime() {
        return validTime;
    }

    public void setValidTime(ArrayList<String> validTime) {
        this.validTime = validTime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
