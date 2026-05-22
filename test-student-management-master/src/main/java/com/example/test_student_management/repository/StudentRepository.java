package com.example.test_student_management.repository;

import com.example.test_student_management.model.Student;
import com.example.test_student_management.util.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    public void insert(Student student) {
        String sql = "INSERT INTO students (student_id, name, email, phone, course, year, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getStudentNumber());
            ps.setString(2, student.getFirstName() + " " + student.getLastName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getPhone());
            ps.setString(5, student.getCourse());
            ps.setInt(6, student.getYearLevel());
            ps.setString(7, "Active");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Student student) {
        String sql = "UPDATE students SET student_id = ?, name = ?, email = ?, phone = ?, course = ?, year = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getStudentNumber());
            ps.setString(2, student.getFirstName() + " " + student.getLastName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getPhone());
            ps.setString(5, student.getCourse());
            ps.setInt(6, student.getYearLevel());
            ps.setInt(7, student.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Student> findPage(int limit, int offset) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT id, student_id, name, email, phone, course, year FROM students ORDER BY id LIMIT ? OFFSET ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStudentNumber(rs.getString("student_id"));
                String fullName = rs.getString("name");
                if (fullName != null) {
                    String[] names = fullName.split(" ", 2);
                    student.setFirstName(names[0]);
                    student.setLastName(names.length > 1 ? names[1] : "");
                }
                student.setEmail(rs.getString("email"));
                student.setPhone(rs.getString("phone"));
                student.setCourse(rs.getString("course"));
                student.setYearLevel(rs.getInt("year"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM students";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}