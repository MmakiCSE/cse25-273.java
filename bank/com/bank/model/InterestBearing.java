package com.bank.model;

/**
 * Defines the behaviour of accounts that earn interest.
 */
public interface InterestBearing {

    double calculateMonthlyInterest();

    double applyMonthlyInterest();
}