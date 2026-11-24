package com.bank.view;

import javax.swing.*;
import java.awt.*;

import com.bank.controller.AccountController;
import com.bank.dao.CustomerDAO;
import com.bank.model.*;

public class BankingGUI extends JFrame {

    private CustomerDAO customerDAO = new CustomerDAO();
    private AccountController accountController = new AccountController();

    private Customer customer;

    // GUI components
    private JTextField firstName;
    private JTextField surname;
    private JTextField address;
    private JTextField username;
    private JPasswordField password;
    private JTextField amount;

    private JComboBox<String> accountType;

    private JTextArea output;

    public BankingGUI() {

        setTitle("Banking System");
        setSize(450, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));

        // Title
        JLabel title = new JLabel("BANKING SYSTEM");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        // Customer details
        firstName = new JTextField();
        firstName.setBorder(BorderFactory.createTitledBorder("First Name"));

        surname = new JTextField();
        surname.setBorder(BorderFactory.createTitledBorder("Surname"));

        address = new JTextField();
        address.setBorder(BorderFactory.createTitledBorder("Address"));

        username = new JTextField();
        username.setBorder(BorderFactory.createTitledBorder("Username"));

        password = new JPasswordField();
        password.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton register = new JButton("Register Customer");

        // Account selection
        accountType = new JComboBox<>();

        accountType.addItem("Savings");
        accountType.addItem("Investment");
        accountType.addItem("Cheque");

        JButton openAccount = new JButton("Open Account");

        // Transaction amount
        amount = new JTextField();
        amount.setBorder(BorderFactory.createTitledBorder("Amount"));

        JButton deposit = new JButton("Deposit");
        JButton withdraw = new JButton("Withdraw");

        // Output
        output = new JTextArea();
        output.setEditable(false);
        output.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(output);

        // REGISTER CUSTOMER
        register.addActionListener(e -> {

            try {

                if (firstName.getText().trim().isEmpty()
                        || surname.getText().trim().isEmpty()
                        || address.getText().trim().isEmpty()
                        || username.getText().trim().isEmpty()
                        || password.getPassword().length == 0) {

                    throw new IllegalArgumentException(
                        "Please complete all customer details."
                    );
                }

                Customer newCustomer = new Customer(
                    0,
                    firstName.getText().trim(),
                    surname.getText().trim(),
                    address.getText().trim(),
                    username.getText().trim(),
                    new String(password.getPassword())
                );

                // Save customer in database
                customer = customerDAO.save(newCustomer);

                output.setText(
                    "Customer registered successfully!\n\n"
                    + "Customer ID: " + customer.getCustomerId()
                    + "\nName: " + customer.getFullName()
                    + "\nUsername: " + customer.getUsername()
                );

            } catch (Exception ex) {

                output.setText("Registration failed: " + ex.getMessage());
            }
        });

        // OPEN ACCOUNT
        openAccount.addActionListener(e -> {

            try {

                if (customer == null) {
                    throw new IllegalArgumentException(
                        "Register a customer first."
                    );
                }

                String type = (String) accountType.getSelectedItem();

                Account account = accountController.openAccount(
                    customer,
                    type.toUpperCase(),
                    type.equals("Investment") ? 500.0 : 0.0,
                    "Gaborone",
                    "",
                    ""
                );

                output.setText(
                    "Account opened successfully!\n\n"
                    + "Account Holder: " + customer.getFullName()
                    + "\nAccount Number: " + account.getAccountNumber()
                    + "\nAccount Type: " + account.getAccountType()
                    + "\nBalance: " + account.getBalance()
                );

            } catch (Exception ex) {

                output.setText("Account error: " + ex.getMessage());
            }
        });

        // DEPOSIT
        deposit.addActionListener(e -> {

            try {

                if (customer == null || customer.getAccounts().isEmpty()) {
                    throw new IllegalArgumentException(
                        "Open an account first."
                    );
                }

                double value = Double.parseDouble(amount.getText());

                Account account = customer.getAccounts().get(0);

                accountController.deposit(
                    account,
                    value,
                    "Cash Deposit"
                );

                output.setText(
                    "Deposit successful!\nNew Balance: "
                    + account.getBalance()
                );

            } catch (Exception ex) {

                output.setText("Deposit error: " + ex.getMessage());
            }
        });

        // WITHDRAW
        withdraw.addActionListener(e -> {

            try {

                if (customer == null || customer.getAccounts().isEmpty()) {
                    throw new IllegalArgumentException(
                        "Open an account first."
                    );
                }

                double value = Double.parseDouble(amount.getText());

                Account account = customer.getAccounts().get(0);

                accountController.withdraw(account, value);

                output.setText(
                    "Withdrawal successful!\nNew Balance: "
                    + account.getBalance()
                );

            } catch (Exception ex) {

                output.setText("Withdrawal error: " + ex.getMessage());
            }
        });

        // ADD COMPONENTS
        panel.add(title);
        panel.add(firstName);
        panel.add(surname);
        panel.add(address);
        panel.add(username);
        panel.add(password);
        panel.add(register);
        panel.add(accountType);
        panel.add(openAccount);
        panel.add(amount);
        panel.add(deposit);
        panel.add(withdraw);
        panel.add(scrollPane);

        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new BankingGUI();
        });
    }
}