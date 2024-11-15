package com.soen342HB.coursecompass.offerings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.soen342HB.coursecompass.core.BaseDAO;

public class BookingDAO extends BaseDAO<Booking> {

    @Override
    public void addtoDb(Booking booking) {
        try {
            startWrite();
        } catch (Exception e) {
            System.out.println("Error occurred while acquiring write lock: " + e.getMessage());
            return;
        }
        String insertBookingSql = booking.getDependentName() != null
                ? "INSERT INTO bookings (user_id, lesson_id, dependent_name, dependent_age) VALUES (?, ?, ?, ?)"
                : "INSERT INTO bookings (user_id, lesson_id) VALUES (?, ?)";

        try (Connection connection = getConnection()) {
            try (PreparedStatement insertStmt = connection.prepareStatement(insertBookingSql)) {
                insertStmt.setInt(1, booking.getUserId());
                insertStmt.setInt(2, booking.getLessonId());
                if (booking.getDependentName() != null) {
                    insertStmt.setString(3, booking.getDependentName());
                    insertStmt.setInt(4, booking.getDependentAge());
                }
                insertStmt.executeUpdate();
                System.out.println("Booking added successfully.");
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }
        endWrite();
    }


    public List<Booking> fetchAllBookingsForUserId(int userId) {
        try {
            startRead();
        } catch (Exception e) {
            System.out.println("Error occurred while acquiring read lock: " + e.getMessage());
            return new ArrayList<>();
        }
        String sql =
                "SELECT id, lesson_id, dependent_name, dependent_age FROM bookings WHERE user_id = ?";
        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int lessonId = resultSet.getInt("lesson_id");
                String dependentName = resultSet.getString("dependent_name");
                int dependentAge = resultSet.getInt("dependent_age");

                Booking booking = new Booking(id, userId, lessonId, dependentName, dependentAge);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }
        endRead();
        return bookings;
    }

    @Override
    public void removeFromDb(Booking booking) {
        try {
            startWrite();
        } catch (Exception e) {
            System.out.println("Error occurred while acquiring write lock: " + e.getMessage());
            return;
        }
        String deleteBookingSql = "DELETE FROM bookings WHERE user_id = ? AND lesson_id = ?";

        try (Connection connection = getConnection();
                PreparedStatement deleteStmt = connection.prepareStatement(deleteBookingSql)) {

            deleteStmt.setInt(1, booking.getUserId());
            deleteStmt.setInt(2, booking.getLessonId());

            int rowsAffected = deleteStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Booking for user ID " + booking.getUserId() + " and lesson ID "
                        + booking.getLessonId() + " deleted successfully.");
            } else {
                System.out.println("No booking found for the specified user ID and lesson ID.");
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred while deleting booking: " + e.getMessage());
        }
        endWrite();
    }

    @Override
    public Booking fetchFromDb(String id) {
        try {
            startRead();
        } catch (Exception e) {
            System.out.println("Error occurred while acquiring read lock: " + e.getMessage());
            return null;
        }
        String fetchBookingSql =
                "SELECT user_id, lesson_id, dependent_name, dependent_age FROM bookings WHERE id = ?";
        Booking booking = null;

        try (Connection connection = getConnection();
                PreparedStatement fetchStmt = connection.prepareStatement(fetchBookingSql)) {

            fetchStmt.setString(1, id);

            try (ResultSet resultSet = fetchStmt.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("user_id");
                    int lessonId = resultSet.getInt("lesson_id");
                    String dependentName = resultSet.getString("dependent_name");
                    int dependentAge = resultSet.getInt("dependent_age");

                    booking = new Booking(userId, lessonId, dependentName, dependentAge);
                } else {
                    System.out.println("No booking found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred while fetching booking: " + e.getMessage());
        }

        endRead();
        return booking;
    }

    public Booking fetchFromDbByValues(int userId, int lessonId) {
        try {
            startRead();
        } catch (Exception e) {
            System.out.println("Error occurred while acquiring read lock: " + e.getMessage());
            return null;
        }
        String fetchBookingSql =
                "SELECT id, dependent_name, dependent_age FROM bookings WHERE user_id = ? AND lesson_id = ?";
        Booking booking = null;

        try (Connection connection = getConnection();
                PreparedStatement fetchStmt = connection.prepareStatement(fetchBookingSql)) {

            fetchStmt.setInt(1, userId);
            fetchStmt.setInt(2, lessonId);

            try (ResultSet resultSet = fetchStmt.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String dependentName = resultSet.getString("dependent_name");
                    int dependentAge = resultSet.getInt("dependent_age");

                    booking = new Booking(id, userId, lessonId, dependentName, dependentAge);
                } else {
                    System.out.println("No booking found for user ID " + userId + " and lesson ID "
                            + lessonId);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred while fetching booking: " + e.getMessage());
        }

        endRead();
        return booking;
    }

    public List<Booking> fetchAllFromDb() {
        try {
            startRead();
        } catch (Exception e) {
            System.out.println("Error occurred while acquiring read lock: " + e.getMessage());
            return new ArrayList<>();
        }
        String fetchAllBookingsSql =
                "SELECT id, user_id, lesson_id, dependent_name, dependent_age FROM bookings";
        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = getConnection();
                PreparedStatement fetchAllStmt = connection.prepareStatement(fetchAllBookingsSql);
                ResultSet resultSet = fetchAllStmt.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("user_id");
                int lessonId = resultSet.getInt("lesson_id");
                String dependentName = resultSet.getString("dependent_name");
                int dependentAge = resultSet.getInt("dependent_age");

                Booking booking = new Booking(id, userId, lessonId, dependentName, dependentAge);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred while fetching all bookings: " + e.getMessage());
        }

        endRead();

        return bookings;
    }

    @Override
    public void updateDb(Booking t) {
        try {
            startWrite();
        } catch (Exception e) {
            System.out.println("Error occurred while acquiring write lock: " + e.getMessage());
            return;
        }
        String updateBookingSql =
                "UPDATE bookings SET dependent_name = ?, dependent_age = ? WHERE id = ?";

        try (Connection connection = getConnection();
                PreparedStatement updateStmt = connection.prepareStatement(updateBookingSql)) {

            updateStmt.setString(1, t.getDependentName());
            updateStmt.setInt(2, t.getDependentAge());
            updateStmt.setInt(3, t.getId());

            int rowsAffected = updateStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Booking with ID " + t.getId() + " updated successfully.");
            } else {
                System.out.println("No booking found with ID " + t.getId());
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred while updating booking: " + e.getMessage());
        }

        endWrite();
    }


}
