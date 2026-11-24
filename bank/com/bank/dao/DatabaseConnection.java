package com.bank.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages JDBC connections to the embedded H2 database, creates the schema,
 * and inserts 10 sample records for demonstration.
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:h2:./bankingdb;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        String createCustomersTable = """
            CREATE TABLE IF NOT EXISTS customers (
                customer_id INT AUTO_INCREMENT PRIMARY KEY,
                firstname VARCHAR(100) NOT NULL,
                surname VARCHAR(100) NOT NULL,
                address VARCHAR(255) NOT NULL,
                username VARCHAR(50) UNIQUE NOT NULL,
                password VARCHAR(100) NOT NULL
            );
            """;

        String createAccountsTable = """
            CREATE TABLE IF NOT EXISTS accounts (
                account_number VARCHAR(30) PRIMARY KEY,
                customer_id INT NOT NULL,
                account_type VARCHAR(20) NOT NULL,
                balance DOUBLE NOT NULL,
                branch VARCHAR(100) NOT NULL,
                company_name VARCHAR(150),
                company_address VARCHAR(255),
                FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
            );
            """;

        String createTransactionsTable = """
            CREATE TABLE IF NOT EXISTS transactions (
                transaction_id INT AUTO_INCREMENT PRIMARY KEY,
                account_number VARCHAR(30) NOT NULL,
                type VARCHAR(20) NOT NULL,
                amount DOUBLE NOT NULL,
                description VARCHAR(255),
                timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (account_number) REFERENCES accounts(account_number)
            );
            """;

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createCustomersTable);
            stmt.execute(createAccountsTable);
            stmt.execute(createTransactionsTable);

            // Seed 10 sample records if customers table is empty
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM customers");
            if (rs.next() && rs.getInt(1) == 0) {
                seedSampleRecords(stmt);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database: " + e.getMessage(), e);
        }
    }

    private static void seedSampleRecords(Statement stmt) throws SQLException {
        // 3 Sample Customers
        stmt.execute("INSERT INTO customers (firstname, surname, address, username, password) VALUES " +
                "('Thabo', 'Molefe', 'Plot 123, Gaborone', 'thabo', 'pass123')," +
                "('Kabo', 'Seretse', 'Plot 456, Francistown', 'kabo', 'pass123')," +
                "('Lesego', 'Khumalo', 'Plot 789, Maun', 'lesego', 'pass123')");

        // 4 Sample Accounts
        stmt.execute("INSERT INTO accounts VALUES " +
                "('SAV-1001', 1, 'SAVINGS', 2500.00, 'Gaborone Main', NULL, NULL)," +
                "('INV-1002', 1, 'INVESTMENT', 1500.00, 'Gaborone Main', NULL, NULL)," +
                "('CHQ-1003', 1, 'CHEQUE', 8200.00, 'Gaborone Main', 'Botswana Power Corp', 'Motlakase House, Gaborone')," +
                "('SAV-2001', 2, 'SAVINGS', 4000.00, 'Francistown Branch', NULL, NULL)");

        // 3 Sample Transactions (Total = 10 sample records across the database)
        stmt.execute("INSERT INTO transactions (account_number, type, amount, description) VALUES " +
                "('SAV-1001', 'DEPOSIT', 2500.00, 'Opening Savings Deposit')," +
                "('INV-1002', 'DEPOSIT', 1500.00, 'Opening Investment Deposit')," +
                "('CHQ-1003', 'DEPOSIT', 8200.00, 'Monthly Salary Credit')");
    }
}