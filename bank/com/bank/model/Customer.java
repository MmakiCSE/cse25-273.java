package com.bank.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Bank Customer who can own multiple accounts.
 */
public class Customer {
    private int customerId;
    private String firstname;
    private String surname;
    private String address;
    private String username;
    private String password;
    private List<Account> accounts;

    public Customer(int customerId, String firstname, String surname, String address,
                    String username, String password) {
        this.customerId = customerId;
        this.firstname = firstname;
        this.surname = surname;
        this.address = address;
        this.username = username;
        this.password = password;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        if (account != null) {
            this.accounts.add(account);
        }
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = new ArrayList<>(accounts);
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getFirstname() { return firstname; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public String getFullName() {
        return firstname + " " + surname;
    }
}