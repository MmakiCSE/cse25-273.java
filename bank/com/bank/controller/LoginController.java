package com.bank.controller;

import com.bank.dao.AccountDAO;
import com.bank.dao.CustomerDAO;
import com.bank.model.Customer;

/**
 * Mediates login requests between LoginView and CustomerDAO/AccountDAO.
 */
public class LoginController {
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final AccountDAO accountDAO = new AccountDAO();

    public Customer handleLogin(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Please enter both username and password.");
        }
        Customer customer = customerDAO.authenticate(username.trim(), password.trim());
        if (customer == null) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
        // Load all accounts belonging to this customer
        customer.setAccounts(accountDAO.findByCustomer(customer));
        return customer;
    }
}