package com.soen342HB.coursecompass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InstructorDAO {
    private static final String URL = "jdbc:mysql://localhost:3308/CourseCompass_db?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "toor";

    public void addInstructortoDb(Instructor instructor) {
        String userSql = "INSERT INTO users (username, password, user_type) VALUES (?, ?, 'INSTRUCTOR')";
<<<<<<< HEAD
        String instructorSql = "INSERT INTO instructors (user_id, specialization) VALUES (?, ?)"; 
=======
        String instructorSql = "INSERT INTO instructors (user_id, specialization) VALUES (?, ?)"; // Keep the
                                                                                                  // RETURN_GENERATED_KEYS
                                                                                                  // here
>>>>>>> 778fec9968e9a45887ea71cf38d344fc74748a60
        String citySql = "INSERT INTO city (city_name) VALUES (?)";
        String linkCitySql = "INSERT INTO instructor_cities (instructor_id, city_id) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement userStatement = connection.prepareStatement(userSql,
                        PreparedStatement.RETURN_GENERATED_KEYS);
                PreparedStatement instructorStatement = connection.prepareStatement(instructorSql,
                        PreparedStatement.RETURN_GENERATED_KEYS); // Request generated keys here
                PreparedStatement cityStatement = connection.prepareStatement(citySql,
                        PreparedStatement.RETURN_GENERATED_KEYS);
                PreparedStatement linkCityStatement = connection.prepareStatement(linkCitySql)) {

            // Insert the user
            userStatement.setString(1, instructor.getUsername());
            userStatement.setString(2, instructor.getPassword());
            int userRowsAffected = userStatement.executeUpdate();

            if (userRowsAffected > 0) {
                // Retrieve the generated user ID
                ResultSet generatedKeys = userStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);

                    // Insert the instructor details
                    instructorStatement.setInt(1, userId);
                    instructorStatement.setString(2, instructor.getSpecialization());
                    int instructorRowsAffected = instructorStatement.executeUpdate();

                    if (instructorRowsAffected > 0) {
                        // Retrieve the generated instructor ID
                        ResultSet instructorGeneratedKeys = instructorStatement.getGeneratedKeys();
                        if (instructorGeneratedKeys.next()) {
                            int instructorId = instructorGeneratedKeys.getInt(1);

                            // Insert cities and link them
                            for (String cityName : instructor.getCityNames()) {
                                // Check if the city already exists (to avoid duplicates)
                                int cityId = insertCityIfNotExists(cityName, cityStatement);

                                // Link the city to the instructor
                                linkCityStatement.setInt(1, instructorId);
                                linkCityStatement.setInt(2, cityId);
                                linkCityStatement.executeUpdate();
                            }
                        }
                        System.out.println("Instructor and cities added successfully.");
                    } else {
                        System.out.println("Failed to add instructor details.");
                    }
                }
            } else {
                System.out.println("Failed to add user.");
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }
    }

    private int insertCityIfNotExists(String cityName, PreparedStatement cityStatement) throws SQLException {
        // Check if the city already exists
        String checkCitySql = "SELECT id FROM city WHERE city_name = ?";
        try (PreparedStatement checkCityStatement = cityStatement.getConnection().prepareStatement(checkCitySql)) {
            checkCityStatement.setString(1, cityName);
            ResultSet rs = checkCityStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("id"); // Return existing city ID
            }
        }

        // If city does not exist, insert it
        cityStatement.setString(1, cityName);
        cityStatement.executeUpdate();

        // Retrieve the generated city ID
        ResultSet generatedKeys = cityStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        }

        throw new SQLException("Failed to insert city: " + cityName);
    }
<<<<<<< HEAD
public Instructor authenticateInstructor(String username, String password) {
    String sql = "SELECT u.id, u.username, u.password, i.specialization, c.city_name " +
                 "FROM users u " +
                 "JOIN instructors i ON u.id = i.user_id " +
                 "LEFT JOIN instructor_cities ic ON i.id = ic.instructor_id " +
                 "LEFT JOIN city c ON ic.city_id = c.id " +
                 "WHERE u.username = ? AND u.password = ? AND u.user_type = 'INSTRUCTOR'";

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();

        Instructor instructor = null;


        while (resultSet.next()) {
            if (instructor == null) {
                // Create the instructor object only once
                instructor = new Instructor();
                Instructor.setUsername(resultSet.getString("username"));
                Instructor.setPassword(resultSet.getString("password"));
                Instructor.setSpecialization(resultSet.getString("specialization"));
            }
            // Collect all city names
            String cities="";
            
            String cityName = resultSet.getString("city_name");
            if (cityName != null) {
                cities+=cityName+",";
            }
            String[] instructorCities = cities.split(",");
            Instructor.setCityNames(instructorCities);
        }
        System.out.println("Succesfully authenticated, welcome back "+ Instructor.getUsername());
        return instructor; // Return the instructor object (null if authentication fails)

    } catch (SQLException e) {
        System.out.println("SQL error occurred during authentication: " + e.getMessage());
=======

    public void instructorAuthentication(Instructor instructor) {

>>>>>>> 778fec9968e9a45887ea71cf38d344fc74748a60
    }

    return null; // Return null if authentication fails
}

    
}
