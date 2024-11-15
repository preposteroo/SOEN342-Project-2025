package com.soen342HB.coursecompass.offerings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.soen342HB.coursecompass.core.BaseDAO;
import com.soen342HB.coursecompass.spaces.City;
import com.soen342HB.coursecompass.spaces.CityDAO;
import com.soen342HB.coursecompass.spaces.Location;
import com.soen342HB.coursecompass.spaces.LocationDAO;
import com.soen342HB.coursecompass.spaces.Space;
import com.soen342HB.coursecompass.spaces.SpaceDAO;
import com.soen342HB.coursecompass.users.InstructorDAO;

public class LessonDAO extends BaseDAO<Lesson> {

    public LessonDAO() {}

    public void addtoDb(Lesson lesson) {
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        scheduleDAO.scheduleToInstructor(lesson.getSchedule(), lesson.getInstructor());
    }

    public Lesson fetchFromDb(String lessonId) {
        String sql =
                "SELECT id, instructor_id, schedule_id, availability FROM lessons WHERE id = ?";
        Lesson lesson = null;
        InstructorDAO instructorDAO = new InstructorDAO();
        ScheduleDAO scheduleDAO = new ScheduleDAO();

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, lessonId); // Set the lesson ID parameter

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int instructorId = resultSet.getInt("instructor_id");
                    int scheduleId = resultSet.getInt("schedule_id");
                    String availability = resultSet.getString("availability");

                    lesson = new Lesson(String.valueOf(id),
                            scheduleDAO.fetchFromDb(String.valueOf(scheduleId)), // Fetch the //
                                                                                 // schedule
                            instructorDAO.fetchFromDb(instructorId), // Fetch the instructor
                            availability);
                } else {
                    System.out.println("Lesson with ID " + lessonId + " not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }

        return lesson; // Return the lesson or null if not found
    }


    public List<Lesson> fetchAllFromDb() {
        String sql = "SELECT id, instructor_id, schedule_id, availability FROM lessons";
        List<Lesson> lessons = new ArrayList<>();
        InstructorDAO instructorDAO = new InstructorDAO();
        ScheduleDAO scheduleDAO = new ScheduleDAO();

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int instructorId = resultSet.getInt("instructor_id");
                int scheduleId = resultSet.getInt("schedule_id");
                String availability = resultSet.getString("availability");

                Lesson lesson = new Lesson(String.valueOf(id),
                        scheduleDAO.fetchFromDb(String.valueOf(scheduleId)),
                        instructorDAO.fetchFromDb(instructorId), availability);
                lessons.add(lesson);
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }

        return lessons;
    }

    public void printLessons(List<Lesson> lessons) {
        CityDAO cityDAO = new CityDAO();
        LocationDAO locationDAO = new LocationDAO();
        SpaceDAO spaceDAO = new SpaceDAO();
        OfferingDAO offeringDAO = new OfferingDAO();
        City[] cities = cityDAO.fetchAllFromDb();
        for (City city : cities) {
            city.setLocations(cityDAO.getLocationsForCity(city));
            if (city.getLocations().isEmpty()) {
                continue;
            }
            System.out.println("Lessons in " + city.getCityName().toUpperCase());
            for (Location location : city.getLocations()) {
                location.setSpaces(locationDAO.getSpacesForLocation(location));
                for (Space space : location.getSpaces()) {
                    space.setSchedules(spaceDAO.getSchedulesForSpace(space));
                    for (Lesson lesson : lessons) {
                        Schedule lessonSchedule = lesson.getSchedule();
                        for (Schedule schedule : space.getSchedules()) {
                            if (lessonSchedule.getId() == schedule.getId()) {
                                Offering offering = offeringDAO.fetchFromDb(String.valueOf(
                                        offeringDAO.getOfferingIdByScheduleId(schedule.getId())));
                                System.out.println(lesson.getId() + ".  "
                                        + lesson.getAvailable().toUpperCase() + " "
                                        + offering.getCourseName() + " class ," + offering.getType()
                                        + " taught by " + lesson.getInstructor().getUsername()
                                        + ". " + "\n" + "Located in " + location.getLocationName()
                                        + " at " + space.getSpaceName() + " between "
                                        + schedule.getStartDate() + " - " + schedule.getEndDate()
                                        + " from " + schedule.getStartTime() + " -  "
                                        + schedule.getEndTime() + " on " + schedule.getDayOfWeek());
                                System.out.println();
                                break;
                            }
                        }
                    }

                }
            }
        }


    }

    public void updateAvailability(int instructorId, int scheduleId, String availability) {
        String updateAvailabilitySql =
                "UPDATE lessons SET availability = ? WHERE instructor_id = ? AND schedule_id = ?";

        try (Connection connection = getConnection()) {
            try (PreparedStatement updateStmt =
                    connection.prepareStatement(updateAvailabilitySql)) {
                updateStmt.setString(1, availability);
                updateStmt.setInt(2, instructorId);
                updateStmt.setInt(3, scheduleId);

                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Availability updated successfully.");
                } else {
                    System.out.println("No matching record found to update.");
                }
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }
    }

    /*
     * public void addBookingToDb(int userId, int lessonId, String dependentName, int dependentAge)
     * { String insertBookingSql =
     * "INSERT INTO bookings (user_id, lesson_id, dependent_name, dependent_age) VALUES (?, ?, ?, ?)"
     * ;
     * 
     * try (Connection connection = getConnection()) { try (PreparedStatement insertStmt =
     * connection.prepareStatement(insertBookingSql)) { insertStmt.setInt(1, userId);
     * insertStmt.setInt(2, lessonId); insertStmt.setString(3, dependentName); insertStmt.setInt(4,
     * dependentAge); insertStmt.executeUpdate(); System.out.println("Booking added successfully.");
     * } catch (SQLException e) { System.out.println("SQL error occurred: " + e.getMessage()); } }
     * catch (SQLException e) { System.out.println("Connection error occurred: " + e.getMessage());
     * } }
     */

    /*
     * public List<Booking> fetchAllBookingsForUserId(int userId) { String sql =
     * "SELECT lesson_id, dependent_name, dependent_age FROM bookings WHERE user_id = ?";
     * List<Booking> bookings = new ArrayList<>();
     * 
     * try (Connection connection = getConnection(); PreparedStatement statement =
     * connection.prepareStatement(sql)) { statement.setInt(1, userId); ResultSet resultSet =
     * statement.executeQuery();
     * 
     * while (resultSet.next()) { int lessonId = resultSet.getInt("lesson_id"); String dependentName
     * = resultSet.getString("dependent_name"); int dependentAge =
     * resultSet.getInt("dependent_age");
     * 
     * Booking booking = new Booking(userId, lessonId, dependentName, dependentAge);
     * bookings.add(booking); } } catch (SQLException e) { System.out.println("SQL error occurred: "
     * + e.getMessage()); }
     * 
     * return bookings; }
     */



    public void updateDb(Lesson lesson) {}

    public void removeFromDb(Lesson lesson) {
        String deleteLessonSql = "DELETE FROM lessons WHERE id = ?";

        try (Connection connection = getConnection();
                PreparedStatement deleteStmt = connection.prepareStatement(deleteLessonSql)) {

            deleteStmt.setString(1, lesson.getId());

            int rowsAffected = deleteStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Lesson with ID " + lesson.getId() + " deleted successfully.");
            } else {
                System.out.println("No lesson found with ID: " + lesson.getId());
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred while deleting lesson: " + e.getMessage());
        }
    }

}
