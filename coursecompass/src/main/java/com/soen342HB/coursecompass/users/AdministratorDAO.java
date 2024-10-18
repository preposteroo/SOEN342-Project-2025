package com.soen342HB.coursecompass.users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.soen342HB.coursecompass.core.IDAO;

public class AdministratorDAO implements IDAO<Administrator> {
    private static final String URL =
            "jdbc:mysql://localhost:3308/CourseCompass_db?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "toor";

    @Override
    public void addtoDb(Administrator administrator) {
        String sql =
                "INSERT INTO users (username, password, user_type) VALUES (?, ?, 'ADMINISTRATOR')";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, administrator.getUsername());
            statement.setString(2, administrator.getPassword());
            statement.executeUpdate();
            System.out.println("Administrator added successfully.");
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }
    }

    @Override
    public void removeFromDb(Administrator administrator) {
        String sql = "DELETE FROM users WHERE username = ? AND user_type = 'ADMINISTRATOR'";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, administrator.getUsername());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Administrator removed successfully.");
            } else {
                System.out.println("Administrator not found.");
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }
    }

    @Override
    public Administrator fetchFromDb(String username) {
        String sql = "SELECT * FROM administrators WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String password = resultSet.getString("password");
                return new Administrator(username, password);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void updateDb(Administrator administrator) {
        String sql =
                "UPDATE users SET password = ? WHERE username = ? AND user_type = 'ADMINISTRATOR'";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, administrator.getPassword());
            statement.setString(2, administrator.getUsername());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Administrator updated successfully.");
            } else {
                System.out.println("Administrator not found.");
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }
    }

    @Override
    public boolean authenticate(String username, String password) {
        String sql =
                "SELECT * FROM users WHERE username = ? AND password = ? AND user_type = 'ADMINISTRATOR'";
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
