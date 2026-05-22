package com.example.test_student_management.controllers;

import com.example.test_student_management.model.Student;
import com.example.test_student_management.repository.StudentRepository;
import com.example.test_student_management.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, Integer> colId;
    @FXML private TableColumn<Student, String> colStudentNumber;
    @FXML private TableColumn<Student, String> colFirstName;
    @FXML private TableColumn<Student, String> colLastName;
    @FXML private TableColumn<Student, String> colCourse;
    @FXML private TableColumn<Student, Integer> colYearLevel;
    @FXML private TableColumn<Student, String> colEmail;
    @FXML private TableColumn<Student, String> colPhone;
    @FXML private TextField searchField;
    @FXML private Label statusLabel;
    @FXML private Label totalStudentsLabel;

    private StudentRepository repository;
    private ObservableList<Student> studentList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        repository = new StudentRepository();
        studentList = FXCollections.observableArrayList();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStudentNumber.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
        colYearLevel.setCellValueFactory(new PropertyValueFactory<>("yearLevel"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        studentTable.setItems(studentList);
        loadStudents();
    }

    private void loadStudents() {
        List<Student> students = repository.findPage(100, 0);
        studentList.setAll(students);
        totalStudentsLabel.setText(String.valueOf(studentList.size()));
        statusLabel.setText("Loaded " + students.size() + " students");
    }

    @FXML
    private void handleRefresh() {
        loadStudents();
        searchField.clear();
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadStudents();
        } else {
            List<Student> results = repository.findPage(100, 0);
            List<Student> filtered = results.stream()
                    .filter(s -> s.getFirstName().toLowerCase().contains(keyword.toLowerCase()) ||
                            s.getLastName().toLowerCase().contains(keyword.toLowerCase()) ||
                            s.getStudentNumber().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
            studentList.setAll(filtered);
            statusLabel.setText("Found " + filtered.size() + " results");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login-view.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);
            Stage stage = (Stage) studentTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            AlertUtil.showError("Navigation Error", "Failed to load login view");
        }
    }
}