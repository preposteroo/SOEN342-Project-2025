package com.soen342HB.coursecompass.users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.soen342HB.coursecompass.core.IDAO;

public class InstructorDAO implements IDAO<Instructor> {
    private static final String URL =
            "jdbc:mysql://localhost:3308/CourseCompass_db?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "toor";

    @Override
    public void addtoDb(Instructor instructor) {
        String sql =
                "INSERT INTO users (username, password, user_type, specialization) VALUES (?, ?, 'INSTRUCTOR', ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, instructor.getUsername());
            statement.setString(2, instructor.getPassword());
            statement.setString(3, instructor.getSpecialization());
            statement.executeUpdate();
            System.out.println("Instructor added successfully.");
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }
    }

    @Override
    public void removeFromDb(Instructor instructor) {
        String sql = "DELETE FROM users WHERE username = ? AND user_type = 'INSTRUCTOR'";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, instructor.getUsername());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Instructor removed successfully.");
            } else {
                System.out.println("Instructor not found.");
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }
    }

    @Override
    public Instructor fetchFromDb(String username) {
        String sql = "SELECT * FROM instructors WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String password = resultSet.getString("password");
                String specialization = resultSet.getString("specialization");
                return new Instructor(username, password, specialization);
            } else {
                System.out.println("Instructor not found.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void updateDb(Instructor instructor) {
        String sql =
                "UPDATE users SET password = ? WHERE username = ? AND user_type = 'INSTRUCTOR'";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, instructor.getPassword());
            statement.setString(2, instructor.getUsername());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Instructor updated successfully.");
            } else {
                System.out.println("Instructor not found.");
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }
    }

    @Override
    public boolean authenticate(String username, String password) {
        String sql =
                "SELECT * FROM users WHERE username = ? AND password = ? AND user_type = 'INSTRUCTOR'";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
            return false;
        }
    }


}
