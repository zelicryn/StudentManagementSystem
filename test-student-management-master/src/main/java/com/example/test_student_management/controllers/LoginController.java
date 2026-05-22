package com.example.test_student_management.controllers;

import com.example.test_student_management.dao.UserDAO;
import com.example.test_student_management.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            AlertUtil.showWarning("Empty Fields", "Please enter both username and password.");
            return;
        }

        boolean isValid = userDAO.validateUser(username, password);

        if (isValid) {
            AlertUtil.showInfo("Login Successful", "Welcome " + username + "!");
            navigateToStudentView();
        } else {
            AlertUtil.showError("Login Failed", "Invalid username or password.");
        }
    }

    @FXML
    private void handleSignup() {
        navigateToSignupView();
    }

    private void navigateToStudentView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/student-view.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 700);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Student Management System");
            stage.show();
        } catch (IOException e) {
            AlertUtil.showError("Navigation Error", "Failed to load student view: " + e.getMessage());
        }
    }

    private void navigateToSignupView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signup-view.fxml"));
            Scene scene = new Scene(loader.load(), 600, 500);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Sign Up");
            stage.show();
        } catch (IOException e) {
            AlertUtil.showError("Navigation Error", "Failed to load signup view: " + e.getMessage());
        }
    }
}