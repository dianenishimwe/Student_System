
package com.example.StudentManagementSystemSpring.repository;

import com.example.StudentManagementSystemSpring.config.DatabaseConnection;
import com.example.StudentManagementSystemSpring.model.Student;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepository {

    private final DatabaseConnection databaseConnection;

    public StudentRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public void save(Student student) {
        String fullName = student.getFirstName() + " " + student.getLastName();
        String sql = "INSERT INTO students (full_name, age, email) VALUES (?, ?, ?) RETURNING id";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, fullName);
            preparedStatement.setInt(2, student.getAge());
            preparedStatement.setString(3, student.getEmail());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    student.setId(resultSet.getInt("id"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving student", e);
        }
    }

    public Student findById(int id) {
        String sql = "SELECT id, full_name, age, email FROM students WHERE id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String fullName = resultSet.getString("full_name");
                    String[] names = fullName.split(" ", 2);

                    return new Student(
                            resultSet.getInt("id"),
                            names[0],
                            names.length > 1 ? names[1] : "",
                            resultSet.getString("email"),
                            resultSet.getInt("age")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding student", e);
        }

        return null;
    }

    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT id, full_name, age, email FROM students ORDER BY id";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String fullName = resultSet.getString("full_name");
                String[] names = fullName.split(" ", 2);

                Student student = new Student(
                        resultSet.getInt("id"),
                        names[0],
                        names.length > 1 ? names[1] : "",
                        resultSet.getString("email"),
                        resultSet.getInt("age")
                );

                students.add(student);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving students", e);
        }

        return students;
    }
}
