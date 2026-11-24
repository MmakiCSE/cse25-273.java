package com.bank.controller;

import com.bank.dao.AccountDAO;
import com.bank.model.*;
import java.util.List;
import java.util.UUID;

/**
 * Mediates account creation, deposits, withdrawals, and interest calculation
 * between AccountView, Core Model classes, and AccountDAO.
 */
public class AccountController {
    private final AccountDAO accountDAO = new AccountDAO();

    public Account openAccount(Customer customer, String type, double initialDeposit,
                               String branch, String companyName, String companyAddress) {
        String prefix = type.substring(0, 3).toUpperCase();
        String accNum = prefix + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        Account newAccount = switch (type.toUpperCase()) {
            case "SAVINGS" -> new SavingsAccount(accNum, initialDeposit, branch, customer);
            case "INVESTMENT" -> new InvestmentAccount(accNum, initialDeposit, branch, customer);
            case "CHEQUE" -> new ChequeAccount(accNum, initialDeposit, branch, customer, companyName, companyAddress);
            default -> throw new IllegalArgumentException("Invalid account type selected.");
        };

        accountDAO.save(newAccount);
        customer.addAccount(newAccount);
        return newAccount;
    }

    public void deposit(Account account, double amount, String description) {
        if (description == null || description.isBlank()) {
            account.deposit(amount);
            description = "Deposit";
        } else {
            account.deposit(amount, description);
        }
        accountDAO.updateBalance(account);
        accountDAO.recordTransaction(account.getAccountNumber(), "DEPOSIT", amount, description);
    }

    public void withdraw(Account account, double amount) {
        // Polymorphic call: SavingsAccount will throw UnsupportedOperationException
        account.withdraw(amount);
        accountDAO.updateBalance(account);
        accountDAO.recordTransaction(account.getAccountNumber(), "WITHDRAWAL", amount, "Cash Withdrawal");
    }

    public double payMonthlyInterest(Account account) {
        if (account instanceof InterestBearing interestAccount) {
            double interest = interestAccount.applyMonthlyInterest();
            accountDAO.updateBalance(account);
            accountDAO.recordTransaction(
                account.getAccountNumber(), "INTEREST", interest, "Monthly Interest Credited"
            );
            return interest;
        } else {
            throw new UnsupportedOperationException(
                account.getAccountType() + " accounts do not earn monthly interest."
            );
        }
    }

    public List<Transaction> getTransactionHistory(Account account) {
        return accountDAO.findTransactionsByAccount(account.getAccountNumber());
    }
}