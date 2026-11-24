package com.bank.view;

import com.bank.controller.LoginController;
import com.bank.model.Customer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Boundary Class for User Login. Contains NO business logic.
 */
public class LoginView {
    private final LoginController loginController = new LoginController();

    public void show(Stage stage) {
        Label titleLabel = new Label("Botswana Digital Banking System");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label hintLabel = new Label("Sample Login: username 'thabo' / password 'pass123'");
        hintLabel.setStyle("-fx-text-fill: #555555;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red;");

        Button loginButton = new Button("Login");
        loginButton.setMaxWidth(Double.MAX_VALUE);

        loginButton.setOnAction(e -> {
            try {
                Customer customer = loginController.handleLogin(
                    usernameField.getText(), passwordField.getText()
                );
                new AccountView(customer).show(stage);
            } catch (Exception ex) {
                statusLabel.setText(ex.getMessage());
            }
        });

        VBox layout = new VBox(12, titleLabel, hintLabel, usernameField, passwordField, loginButton, statusLabel);
        layout.setPadding(new Insets(25));
        layout.setAlignment(Pos.CENTER);

        stage.setTitle("Login - OOAD Banking System");
        stage.setScene(new Scene(layout, 400, 300));
        stage.show();
    }
}