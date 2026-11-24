package com.bank.model;

/**
 * Savings Account:
 * - Allows deposits.
 * - Prohibits all withdrawals.
 * - Pays 0.05% monthly interest.
 */
public class SavingsAccount extends Account implements InterestBearing {
    private static final double MONTHLY_INTEREST_RATE = 0.0005; // 0.05%

    public SavingsAccount(String accountNumber, double initialBalance, String branch, Customer customer) {
        super(accountNumber, initialBalance, branch, customer);
    }

    @Override
    public void withdraw(double amount) {
        throw new UnsupportedOperationException("Withdrawals are not permitted on a Savings Account.");
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
        return "SAVINGS";
    }
}