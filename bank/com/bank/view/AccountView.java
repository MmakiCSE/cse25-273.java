package com.bank.view;

import com.bank.controller.AccountController;
import com.bank.model.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Boundary Class for Customer Accounts Dashboard. Contains NO business logic.
 */
public class AccountView {
    private final Customer customer;
    private final AccountController accountController = new AccountController();

    private ComboBox<String> accountSelector;
    private Label balanceLabel;
    private Label detailsLabel;
    private ListView<String> transactionList;

    public AccountView(Customer customer) {
        this.customer = customer;
    }

    public void show(Stage stage) {
        Label welcomeLabel = new Label("Welcome, " + customer.getFullName() + " (" + customer.getAddress() + ")");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        accountSelector = new ComboBox<>();
        balanceLabel = new Label("Balance: BWP 0.00");
        balanceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        detailsLabel = new Label();

        refreshAccountDropdown();
        accountSelector.setOnAction(e -> updateSelectedAccountDisplay());

        // Deposit & Withdraw Controls
        TextField amountField = new TextField();
        amountField.setPromptText("Amount (BWP)");

        TextField descField = new TextField();
        descField.setPromptText("Description (optional, e.g., Salary)");

        Button depositBtn = new Button("Deposit");
        Button withdrawBtn = new Button("Withdraw");
        Button interestBtn = new Button("Apply Monthly Interest");

        depositBtn.setOnAction(e -> handleDeposit(amountField, descField));
        withdrawBtn.setOnAction(e -> handleWithdraw(amountField));
        interestBtn.setOnAction(e -> handleInterest());

        HBox transactionControls = new HBox(10, amountField, descField, depositBtn, withdrawBtn, interestBtn);

        // Open New Account Section
        ComboBox<String> newTypeBox = new ComboBox<>();
        newTypeBox.getItems().addAll("SAVINGS", "INVESTMENT", "CHEQUE");
        newTypeBox.setValue("SAVINGS");

        TextField initDepositField = new TextField();
        initDepositField.setPromptText("Initial Deposit (Min 500 for Investment)");

        TextField branchField = new TextField("Gaborone Main");
        TextField companyField = new TextField();
        companyField.setPromptText("Company Name (Cheque only)");
        TextField companyAddrField = new TextField();
        companyAddrField.setPromptText("Company Address (Cheque only)");

        Button openAccBtn = new Button("Open New Account");
        openAccBtn.setOnAction(e -> handleOpenAccount(
            newTypeBox.getValue(), initDepositField, branchField, companyField, companyAddrField
        ));

        HBox openAccountRow1 = new HBox(10, newTypeBox, initDepositField, branchField);
        HBox openAccountRow2 = new HBox(10, companyField, companyAddrField, openAccBtn);

        transactionList = new ListView<>();
        transactionList.setPrefHeight(180);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> new LoginView().show(stage));

        VBox root = new VBox(12,
            welcomeLabel,
            new HBox(10, new Label("Select Account:"), accountSelector, logoutBtn),
            balanceLabel,
            detailsLabel,
            new Separator(),
            new Label("Transactions:"),
            transactionControls,
            new Separator(),
            new Label("Open Another Account:"),
            openAccountRow1,
            openAccountRow2,
            new Separator(),
            new Label("Transaction History:"),
            transactionList
        );
        root.setPadding(new Insets(20));

        updateSelectedAccountDisplay();

        stage.setTitle("Account Dashboard - OOAD Banking System");
        stage.setScene(new Scene(root, 780, 560));
        stage.show();
    }

    private Account getSelectedAccount() {
        int idx = accountSelector.getSelectionModel().getSelectedIndex();
        if (idx >= 0 && idx < customer.getAccounts().size()) {
            return customer.getAccounts().get(idx);
        }
        return null;
    }

    private void refreshAccountDropdown() {
        accountSelector.getItems().clear();
        for (Account acc : customer.getAccounts()) {
            accountSelector.getItems().add(acc.getAccountNumber() + " (" + acc.getAccountType() + ")");
        }
        if (!accountSelector.getItems().isEmpty()) {
            accountSelector.getSelectionModel().selectFirst();
        }
    }

    private void updateSelectedAccountDisplay() {
        Account acc = getSelectedAccount();
        if (acc == null) return;

        balanceLabel.setText(String.format("Current Balance: BWP %.2f", acc.getBalance()));
        if (acc instanceof ChequeAccount cheque) {
            detailsLabel.setText("Branch: " + acc.getBranch() + " | Employer: " +
                cheque.getCompanyName() + " (" + cheque.getCompanyAddress() + ")");
        } else {
            detailsLabel.setText("Branch: " + acc.getBranch());
        }

        transactionList.getItems().clear();
        for (Transaction t : accountController.getTransactionHistory(acc)) {
            transactionList.getItems().add(t.toString());
        }
    }

    private void handleDeposit(TextField amountField, TextField descField) {
        try {
            Account acc = getSelectedAccount();
            double amount = Double.parseDouble(amountField.getText());
            accountController.deposit(acc, amount, descField.getText());
            amountField.clear();
            descField.clear();
            updateSelectedAccountDisplay();
        } catch (Exception ex) {
            showAlert("Deposit Error", ex.getMessage());
        }
    }

    private void handleWithdraw(TextField amountField) {
        try {
            Account acc = getSelectedAccount();
            double amount = Double.parseDouble(amountField.getText());
            accountController.withdraw(acc, amount);
            amountField.clear();
            updateSelectedAccountDisplay();
        } catch (Exception ex) {
            showAlert("Withdrawal Error", ex.getMessage());
        }
    }

    private void handleInterest() {
        try {
            Account acc = getSelectedAccount();
            double interest = accountController.payMonthlyInterest(acc);
            showAlert("Interest Paid", String.format("Monthly interest of BWP %.2f credited!", interest));
            updateSelectedAccountDisplay();
        } catch (Exception ex) {
            showAlert("Interest Notice", ex.getMessage());
        }
    }

    private void handleOpenAccount(String type, TextField initField, TextField branchField,
                                   TextField compField, TextField addrField) {
        try {
            double initDeposit = Double.parseDouble(initField.getText());
            accountController.openAccount(
                customer, type, initDeposit, branchField.getText(),
                compField.getText(), addrField.getText()
            );
            refreshAccountDropdown();
            accountSelector.getSelectionModel().selectLast();
            updateSelectedAccountDisplay();
            initField.clear();
            compField.clear();
            addrField.clear();
        } catch (Exception ex) {
            showAlert("Account Creation Error", ex.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}