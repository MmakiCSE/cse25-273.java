package com.bank.dao;

import com.bank.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Account and Transaction CRUD operations.
 */
public class AccountDAO {

    public void save(Account account) {
        String sql = "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, company_name, company_address) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, account.getAccountNumber());
            ps.setInt(2, account.getCustomer().getCustomerId());
            ps.setString(3, account.getAccountType());
            ps.setDouble(4, account.getBalance());
            ps.setString(5, account.getBranch());

            if (account instanceof ChequeAccount cheque) {
                ps.setString(6, cheque.getCompanyName());
                ps.setString(7, cheque.getCompanyAddress());
            } else {
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
            }
            ps.executeUpdate();
            recordTransaction(account.getAccountNumber(), "DEPOSIT", account.getBalance(), "Account Opened");
        } catch (SQLException e) {
            throw new RuntimeException("Error saving account: " + e.getMessage(), e);
        }
    }

    public List<Account> findByCustomer(Customer customer) {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customer.getCustomerId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String accNum = rs.getString("account_number");
                String type = rs.getString("account_type");
                double balance = rs.getDouble("balance");
                String branch = rs.getString("branch");

                Account acc = switch (type) {
                    case "SAVINGS" -> new SavingsAccount(accNum, balance, branch, customer);
                    case "INVESTMENT" -> new InvestmentAccount(accNum, balance, branch, customer);
                    case "CHEQUE" -> new ChequeAccount(
                        accNum, balance, branch, customer,
                        rs.getString("company_name"),
                        rs.getString("company_address")
                    );
                    default -> throw new IllegalStateException("Unknown account type: " + type);
                };
                list.add(acc);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading accounts: " + e.getMessage(), e);
        }
        return list;
    }

    public void updateBalance(Account account) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, account.getBalance());
            ps.setString(2, account.getAccountNumber());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating balance: " + e.getMessage(), e);
        }
    }

    public void recordTransaction(String accountNumber, String type, double amount, String description) {
        String sql = "INSERT INTO transactions (account_number, type, amount, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.setString(4, description);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error recording transaction: " + e.getMessage(), e);
        }
    }

    public List<Transaction> findTransactionsByAccount(String accountNumber) {
        List<Transaction> history = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                history.add(new Transaction(
                    rs.getInt("transaction_id"),
                    rs.getString("account_number"),
                    rs.getString("type"),
                    rs.getDouble("amount"),
                    rs.getString("description"),
                    rs.getTimestamp("timestamp").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching transactions: " + e.getMessage(), e);
        }
        return history;
    }
}