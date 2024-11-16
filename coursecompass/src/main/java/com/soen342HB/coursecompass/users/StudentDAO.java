package com.soen342HB.coursecompass.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.soen342HB.coursecompass.core.BaseDAO;

public class StudentDAO extends BaseDAO<Student> {

    @Override
    public void addtoDb(Student student) {
        String sql = "INSERT INTO users (username, password, user_type) VALUES (?, ?, 'CLIENT')";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, student.getUsername());
            statement.setString(2, student.getPassword());
            statement.executeUpdate();
            System.out.println("Student added successfully.");
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }
    }

    @Override
    public void removeFromDb(Student t) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, t.getId());
            statement.executeUpdate();
            System.out.println("Student removed successfully.");
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }
    }

    @Override
    public Student fetchFromDb(String username) {
        String sql =
                "SELECT id, username, password FROM users WHERE username = ? AND user_type = 'CLIENT'";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String password = resultSet.getString("password");
                return new Student(id, username, password);
            } else {
                System.out.println("Client not found for username: " + username);
                return null;
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
            return null;
        }
    }

    public List<Student> fetchAllFromDb() {
        String sql = "SELECT id, username, password FROM users WHERE user_type = 'CLIENT'";
        List<Student> students = new ArrayList<Student>();
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                students.add(new Student(id, username, password));
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }
        return students;
    }


    @Override
    public void updateDb(Student t) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, t.getPassword());
            statement.setInt(2, t.getId());
            statement.executeUpdate();
            System.out.println("Student updated successfully.");
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }
    }

    public String getUsernameById(int id) {
        String sql = "SELECT username FROM users WHERE id = ?";
        String username = null;
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                username = resultSet.getString("username");
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }
        return username;
    }

}
