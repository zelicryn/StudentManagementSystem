package com.example.test_student_management.controllers;

import com.example.test_student_management.model.Student;
import com.example.test_student_management.repository.StudentRepository;
import com.example.test_student_management.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class StudentController {
    private static final int PAGE_SIZE = 10;

    @FXML private TextField idField;
    @FXML private TextField studentNumberField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField courseField;
    @FXML private TextField yearLevelField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, Integer> idColumn;
    @FXML private TableColumn<Student, String> studentNumberColumn;
    @FXML private TableColumn<Student, String> firstNameColumn;
    @FXML private TableColumn<Student, String> lastNameColumn;
    @FXML private TableColumn<Student, String> courseColumn;
    @FXML private TableColumn<Student, Integer> yearLevelColumn;
    @FXML private TableColumn<Student, String> emailColumn;
    @FXML private TableColumn<Student, String> phoneColumn;
    @FXML private Pagination studentPagination;

    private final StudentRepository repository = new StudentRepository();
    private final ObservableList<Student> students = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentNumberColumn.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        yearLevelColumn.setCellValueFactory(new PropertyValueFactory<>("yearLevel"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        studentTable.setItems(students);
        studentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> populateForm(selected));

        studentPagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            loadPage(newIndex.intValue());
        });
        refreshPagination();
    }

    @FXML
    private void handleAdd() {
        if (!validateForm(true)) {
            return;
        }
        try {
            repository.insert(buildStudent(false));
            clearForm();
            refreshPagination();
            AlertUtil.showInfo("Success", "Student added successfully!");
        } catch (Exception e) {
            AlertUtil.showError("Unable to add student", e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        if (!validateForm(false)) {
            return;
        }
        try {
            repository.update(buildStudent(true));
            clearForm();
            refreshPagination();
            AlertUtil.showInfo("Success", "Student updated successfully!");
        } catch (Exception e) {
            AlertUtil.showError("Unable to update student", e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("No selection", "Choose a student to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Delete");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete " + selected.getFirstName() + " " + selected.getLastName() + "?");

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                repository.delete(selected.getId());
                clearForm();
                refreshPagination();
                AlertUtil.showInfo("Success", "Student deleted successfully!");
            } catch (Exception e) {
                AlertUtil.showError("Unable to delete student", e.getMessage());
            }
        }
    }

    @FXML
    private void handleClear() {
        clearForm();
    }

    private void refreshPagination() {
        int totalStudents = repository.countAll();
        int pageCount = Math.max(1, (int) Math.ceil(totalStudents / (double) PAGE_SIZE));
        studentPagination.setPageCount(pageCount);
        int pageIndex = Math.min(studentPagination.getCurrentPageIndex(), pageCount - 1);
        studentPagination.setCurrentPageIndex(pageIndex);
        loadPage(pageIndex);
    }

    private void loadPage(int pageIndex) {
        int offset = pageIndex * PAGE_SIZE;
        students.setAll(repository.findPage(PAGE_SIZE, offset));
        studentTable.getSelectionModel().clearSelection();
    }

    private Student buildStudent(boolean includeId) {
        Student student = new Student();
        if (includeId && !idField.getText().trim().isEmpty()) {
            student.setId(Integer.parseInt(idField.getText().trim()));
        }
        student.setStudentNumber(studentNumberField.getText().trim());
        student.setFirstName(firstNameField.getText().trim());
        student.setLastName(lastNameField.getText().trim());
        student.setCourse(courseField.getText().trim());
        student.setYearLevel(Integer.parseInt(yearLevelField.getText().trim()));
        student.setEmail(emailField.getText().trim());
        student.setPhone(phoneField.getText().trim());
        return student;
    }

    private boolean validateForm(boolean allowEmptyId) {
        if (!allowEmptyId && idField.getText().trim().isEmpty()) {
            AlertUtil.showWarning("Missing selection", "Select a student from the table first.");
            return false;
        }
        if (studentNumberField.getText().trim().isEmpty()
                || firstNameField.getText().trim().isEmpty()
                || lastNameField.getText().trim().isEmpty()
                || courseField.getText().trim().isEmpty()
                || yearLevelField.getText().trim().isEmpty()) {
            AlertUtil.showWarning("Missing data", "Student number, name, course, and year level are required.");
            return false;
        }
        try {
            Integer.parseInt(yearLevelField.getText().trim());
            if (!allowEmptyId && !idField.getText().trim().isEmpty()) {
                Integer.parseInt(idField.getText().trim());
            }
        } catch (NumberFormatException e) {
            AlertUtil.showWarning("Invalid number", "Year level must be numeric.");
            return false;
        }
        return true;
    }

    private void populateForm(Student student) {
        if (student == null) {
            return;
        }
        idField.setText(String.valueOf(student.getId()));
        studentNumberField.setText(student.getStudentNumber());
        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());
        courseField.setText(student.getCourse());
        yearLevelField.setText(String.valueOf(student.getYearLevel()));
        emailField.setText(student.getEmail());
        phoneField.setText(student.getPhone());
    }

    private void clearForm() {
        idField.clear();
        studentNumberField.clear();
        firstNameField.clear();
        lastNameField.clear();
        courseField.clear();
        yearLevelField.clear();
        emailField.clear();
        phoneField.clear();
        studentTable.getSelectionModel().clearSelection();
    }
}