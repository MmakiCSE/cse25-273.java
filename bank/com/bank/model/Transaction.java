package com.bank.model;

import java.time.LocalDateTime;

/**
 * Represents a financial transaction record for audit and history display.
 */
public class Transaction {
    private int transactionId;
    private String accountNumber;
    private String type; // DEPOSIT, WITHDRAWAL, INTEREST
    private double amount;
    private String description;
    private LocalDateTime timestamp;

    public Transaction(int transactionId, String accountNumber, String type,
                       double amount, String description, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = timestamp;
    }

    public int getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("[%s] %s: BWP %.2f (%s)",
                timestamp.toLocalDate(), type, amount, description);
    }
}