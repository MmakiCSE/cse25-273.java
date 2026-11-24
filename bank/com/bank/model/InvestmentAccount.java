package com.bank.model;

/**
 * Investment Account:
 * - Requires a minimum initial deposit of BWP 500.00 to open.
 * - Allows deposits and withdrawals.
 * - Pays 5% monthly interest.
 */
public class InvestmentAccount extends Account implements InterestBearing {
    public static final double MINIMUM_OPENING_DEPOSIT = 500.00;
    private static final double MONTHLY_INTEREST_RATE = 0.05; // 5%

    public InvestmentAccount(String accountNumber, double initialDeposit, String branch, Customer customer) {
        super(accountNumber, validateOpeningDeposit(initialDeposit), branch, customer);
    }

    private static double validateOpeningDeposit(double amount) {
        if (amount < MINIMUM_OPENING_DEPOSIT) {
            throw new IllegalArgumentException(
                "Investment Account requires an initial deposit of at least BWP 500.00."
            );
        }
        return amount;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > this.balance) {
            throw new IllegalArgumentException("Insufficient funds in Investment Account.");
        }
        this.balance -= amount;
    }

    @Override
    public double calculateMonthlyInterest() {
        return this.balance * MONTHLY_INTEREST_RATE;
    }

    @Override
    public double applyMonthlyInterest() {
        double interest = calculateMonthlyInterest();
        this.balance += interest;
        return interest;
    }

    @Override
    public String getAccountType() {
        return "INVESTMENT";
    }
}