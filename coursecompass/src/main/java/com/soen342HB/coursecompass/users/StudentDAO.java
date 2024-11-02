package com.soen342HB.coursecompass.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.cj.xdevapi.Client;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeFromDb'");
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


    @Override
    public void updateDb(Student t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateDb'");
    }

}
