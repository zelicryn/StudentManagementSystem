package com.example.test_student_management.controllers;

import com.example.test_student_management.dao.UserDAO;
import com.example.test_student_management.model.UserAccount;
import com.example.test_student_management.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class SignupController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleSignup() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            AlertUtil.showWarning("Empty Fields", "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            AlertUtil.showWarning("Password Mismatch", "Passwords do not match.");
            return;
        }

        if (password.length() < 4) {
            AlertUtil.showWarning("Weak Password", "Password must be at least 4 characters.");
            return;
        }

        if (!email.contains("@")) {
            AlertUtil.showWarning("Invalid Email", "Please enter a valid email address.");
            return;
        }

        if (userDAO.usernameExists(username)) {
            AlertUtil.showWarning("Username Taken", "Username already exists. Please choose another.");
            return;
        }

        UserAccount newUser = new UserAccount(username, email, password);
        boolean success = userDAO.createUser(newUser);

        if (success) {
            AlertUtil.showInfo("Signup Successful", "Account created successfully! Please login.");
            navigateToLoginView();
        } else {
            AlertUtil.showError("Signup Failed", "Could not create account. Please try again.");
        }
    }

    @FXML
    private void handleLogin() {
        navigateToLoginView();
    }

    private void navigateToLoginView() {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/login-view.fxml");
            if (fxmlUrl == null) {
                AlertUtil.showError("FXML Error", "login-view.fxml not found in resources/fxml/");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load(), 600, 400);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            AlertUtil.showError("Navigation Error", "Failed to load login view: " + e.getMessage());
        }
    }
}