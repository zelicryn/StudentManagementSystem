package com.example.test_student_management.factory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public final class StudentWindowFactory {
    private StudentWindowFactory() {
    }

    public static Scene createScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(StudentWindowFactory.class.getResource("/src/main/student-view.fxml"));
        return new Scene(loader.load());
    }
}
