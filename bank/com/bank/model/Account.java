package com.bank.model;

import java.util.Objects;

/**
 * Abstract base class representing a generic Bank Account.
 * Demonstrates Abstraction, Encapsulation, and Method Overloading.
 */
public abstract class Account {
    private String accountNumber;
    protected double balance;
    private String branch;
    private Customer customer;

    public Account(String accountNumber, double initialBalance, String branch, Customer customer) {
        // Enforce rule: An account cannot exist without a customer
        this.customer = Objects.requireNonNull(customer, "An account must belong to a customer.");
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number cannot be empty.");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.branch = (branch == null || branch.isBlank()) ? "Main Branch" : branch;
    }

    // Standard deposit method
    public void deposit(double amount) {
        deposit(amount, "Standard Deposit");
    }

    // Overloaded deposit method allowing a custom description (e.g., Salary Credit)
    public void deposit(double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        this.balance += amount;
    }

    // Abstract method: each subclass defines its own withdrawal rule
    public abstract void withdraw(double amount);

    public abstract String getAccountType();

    // Encapsulated Getters
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getBranch() { return branch; }
    public Customer getCustomer() { return customer; }
}