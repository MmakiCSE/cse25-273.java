package com.bank.model;

/**
 * Cheque Account:
 * - Used for salary payments; allows deposits and withdrawals.
 * - Only opened for employed customers (requires company name and address).
 * - Does not earn interest.
 */
public class ChequeAccount extends Account {
    private String companyName;
    private String companyAddress;

    public ChequeAccount(String accountNumber, double initialBalance, String branch,
                         Customer customer, String companyName, String companyAddress) {
        super(accountNumber, initialBalance, branch, customer);
        if (companyName == null || companyName.isBlank() ||
            companyAddress == null || companyAddress.isBlank()) {
            throw new IllegalArgumentException(
                "Cheque Account requires employer company name and company address."
            );
        }
        this.companyName = companyName;
        this.companyAddress = companyAddress;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > this.balance) {
            throw new IllegalArgumentException("Insufficient funds in Cheque Account.");
        }
        this.balance -= amount;
    }

    @Override
    public String getAccountType() {
        return "CHEQUE";
    }

    public String getCompanyName() { return companyName; }
    public String getCompanyAddress() { return companyAddress; }
}