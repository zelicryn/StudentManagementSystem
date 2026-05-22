package com.example.test_student_management.dao;

import com.example.test_student_management.model.Student;
import com.example.test_student_management.util.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public boolean createStudent(Student student) {
        String sql = "INSERT INTO students (student_id, name, email, phone, course, year, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getStudentNumber());
            pstmt.setString(2, student.getFirstName() + " " + student.getLastName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getPhone());
            pstmt.setString(5, student.getCourse());
            pstmt.setInt(6, student.getYearLevel());
            pstmt.setString(7, "Active");
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT id, student_id, name, email, phone, course, year FROM students ORDER BY id DESC";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
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

    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET student_id = ?, name = ?, email = ?, phone = ?, course = ?, year = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getStudentNumber());
            pstmt.setString(2, student.getFirstName() + " " + student.getLastName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getPhone());
            pstmt.setString(5, student.getCourse());
            pstmt.setInt(6, student.getYearLevel());
            pstmt.setInt(7, student.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Student> searchStudents(String keyword) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT id, student_id, name, email, phone, course, year FROM students WHERE name LIKE ? OR student_id LIKE ? OR email LIKE ? OR course LIKE ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            ResultSet rs = pstmt.executeQuery();
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

    public int getTotalStudentCount() {
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