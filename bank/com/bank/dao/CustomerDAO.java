package com.bank.dao;

import com.bank.model.Customer;
import java.sql.*;

/**
 * Data Access Object for Customer CRUD operations.
 */
public class CustomerDAO {

    public Customer authenticate(String username, String password) {
        String sql = "SELECT * FROM customers WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("firstname"),
                    rs.getString("surname"),
                    rs.getString("address"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error authenticating user: " + e.getMessage(), e);
        }
        return null;
    }

    public Customer save(Customer customer) {
        String sql = "INSERT INTO customers (firstname, surname, address, username, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, customer.getFirstname());
            ps.setString(2, customer.getSurname());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getUsername());
            ps.setString(5, customer.getPassword());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                customer.setCustomerId(keys.getInt(1));
            }
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving customer: " + e.getMessage(), e);
        }
    }
}