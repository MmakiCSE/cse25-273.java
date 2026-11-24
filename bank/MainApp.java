package com.bank;

import com.bank.dao.DatabaseConnection;
import com.bank.view.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        DatabaseConnection.initializeDatabase();
        new LoginView().show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}