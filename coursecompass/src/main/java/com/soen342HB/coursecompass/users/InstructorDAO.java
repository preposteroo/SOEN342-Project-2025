package com.soen342HB.coursecompass.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.soen342HB.coursecompass.core.BaseDAO;

public class InstructorDAO extends BaseDAO<Instructor> {

    @Override
    public void addtoDb(Instructor instructor) {
        String insertUserSql =
                "INSERT INTO users (username, password, user_type) VALUES (?, ?, 'INSTRUCTOR')";
        String insertInstructorSql =
                "INSERT INTO instructors (user_id, specialization) VALUES (?, ?)";
        String insertCitySql =
                "INSERT INTO instructor_cities (instructor_id, city_id) VALUES (?, ?)";

        try (Connection connection = getConnection()) {

            try (PreparedStatement userStmt = connection.prepareStatement(insertUserSql,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, instructor.getUsername());
                userStmt.setString(2, instructor.getPassword());
                userStmt.executeUpdate();
                // Get the generated user_id
                ResultSet rs = userStmt.getGeneratedKeys();
                if (rs.next()) {
                    int userId = rs.getInt(1);

                    // Step 2: Insert into 'instructors' table
                    try (PreparedStatement instructorStmt = connection.prepareStatement(
                            insertInstructorSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        instructorStmt.setInt(1, userId);
                        instructorStmt.setString(2, instructor.getSpecialization());
                        instructorStmt.executeUpdate();

                        ResultSet instructorRs = instructorStmt.getGeneratedKeys();
                        if (instructorRs.next()) {
                            int instructorId = instructorRs.getInt(1);

                            try (PreparedStatement cityStmt =
                                    connection.prepareStatement(insertCitySql)) {
                                for (String city : instructor.getCities()) {
                                    int cityId = getOrInsertCityId(city, connection);
                                    cityStmt.setInt(1, instructorId);
                                    cityStmt.setInt(2, cityId);
                                    cityStmt.executeUpdate(); // Directly insert each city
                                }
                            }
                        }
                    }
                }
                System.out.println("Instructor added successfully.");
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }

    }

    private int getOrInsertCityId(String cityName, Connection connection) throws SQLException {
        String query = "SELECT id FROM city WHERE city_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cityName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // City exists, return the existing city ID
                return rs.getInt("id");
            } else {
                // City does not exist, insert it and return the new city ID
                String insertCitySql = "INSERT INTO city (city_name) VALUES (?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertCitySql,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
                    insertStmt.setString(1, cityName);
                    insertStmt.executeUpdate();

                    // Get the generated city ID
                    ResultSet insertRs = insertStmt.getGeneratedKeys();
                    if (insertRs.next()) {
                        return insertRs.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve the city ID after insertion.");
                    }
                }
            }
        }
    }

    @Override
    public void removeFromDb(Instructor instructor) {
        String sql = "DELETE FROM users WHERE username = ? AND user_type = 'INSTRUCTOR'";
        try (Connection connection = getConnection();
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

    /*
     * @Override public Instructor fetchFromDb(String username) { String sql =
     * "SELECT * FROM users WHERE username = ? AND user_type = 'INSTRUCTOR'"; try (Connection
     * connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql))
     * { statement.setString(1, username); ResultSet resultSet = statement.executeQuery(); if
     * (resultSet.next()) { int id = resultSet.getInt("id"); String password =
     * resultSet.getString("password"); // String specialization =
     * resultSet.getString("specialization"); // String cities = resultSet.getString("cities"); //
     * return new Instructor(username, password, specialization, cities.split(",")); return new
     * Instructor(username, password, "math", new String[] {"Montreal"}); } else {
     * System.out.println("Instructor not found."); return null; } } catch (SQLException e) {
     * System.out.println("SQL error occurred: " + e.getMessage()); return null; } }
     */

    @Override
    public Instructor fetchFromDb(String username) {
        String sql = "SELECT i.id AS instructor_id, u.password, u.username, i.specialization "
                + "FROM users u " + "JOIN instructors i ON u.id = i.user_id "
                + "WHERE u.username = ? AND u.user_type = 'INSTRUCTOR'";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int instructorId = resultSet.getInt("instructor_id");
                String password = resultSet.getString("password");
                String specialization = resultSet.getString("specialization");

                // Retrieve available locations
                String[] availableLocations = getInstructorCities(instructorId, connection);

                return new Instructor(instructorId, username, password, specialization,
                        availableLocations);
            } else {
                System.out.println("Instructor not found.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
            return null;
        }
    }

    public Instructor fetchFromDb(int instructorId) {
        String sql = "SELECT i.id AS instructor_id, u.password, u.username, i.specialization "
                + "FROM instructors i " + "JOIN users u ON u.id = i.user_id " + "WHERE i.id = ?";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, instructorId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String specialization = resultSet.getString("specialization");

                String[] availableLocations = getInstructorCities(instructorId, connection);

                return new Instructor(instructorId, username, password, specialization,
                        availableLocations);
            } else {
                System.out.println("Instructor not found.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
            return null;
        }
    }

    private String[] getInstructorCities(int instructorId, Connection connection)
            throws SQLException {
        List<String> cities = new ArrayList<>();
        String query = "SELECT c.city_name FROM instructor_cities ic "
                + "JOIN city c ON ic.city_id = c.id " + "WHERE ic.instructor_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, instructorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cities.add(rs.getString("city_name"));
            }
        }
        return cities.toArray(new String[0]); // Convert List to String array
    }


    @Override
    public void updateDb(Instructor instructor) {
        String sql =
                "UPDATE users SET password = ? WHERE username = ? AND user_type = 'INSTRUCTOR'";
        try (Connection connection = getConnection();
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
}
