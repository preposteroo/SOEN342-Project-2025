package com.soen342HB.coursecompass.offerings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.soen342HB.coursecompass.core.BaseDAO;
import com.soen342HB.coursecompass.spaces.Space;
import com.soen342HB.coursecompass.users.Instructor;
import com.soen342HB.coursecompass.spaces.Location;
import com.soen342HB.coursecompass.spaces.City;

public class ScheduleDAO extends BaseDAO<Schedule> {

    public ScheduleDAO() {}

    @Override
    public void addtoDb(Schedule schedule) {
        String insertScheduleSql =
                "INSERT INTO schedules (start_date, end_date, day_of_week, start_time, end_time, offering_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection()) {
            try (PreparedStatement insertStmt = connection.prepareStatement(insertScheduleSql,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                insertStmt.setDate(1, java.sql.Date.valueOf(schedule.getStartDate()));
                insertStmt.setDate(2, java.sql.Date.valueOf(schedule.getEndDate()));
                insertStmt.setString(3, schedule.getDayOfWeek().name());
                insertStmt.setTime(4, java.sql.Time.valueOf(schedule.getStartTime() + ":00"));
                insertStmt.setTime(5, java.sql.Time.valueOf(schedule.getEndTime() + ":00"));
                insertStmt.setInt(6, schedule.getOfferingId());

                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int newId = generatedKeys.getInt(1);
                            schedule.setId(newId);
                            System.out.println("Schedule added successfully with ID: " + newId);
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }
    }


    public void scheduleToSpace(Space space, Schedule schedule) {
        String insertScheSpaceSql =
                "INSERT INTO spaces_schedules (space_id, schedule_id) VALUES (?, ?)";
        try (Connection connection = getConnection()) {
            try (PreparedStatement insertStmt = connection.prepareStatement(insertScheSpaceSql)) {
                insertStmt.setInt(1, space.getId());
                insertStmt.setInt(2, schedule.getId());
                insertStmt.executeUpdate();
                System.out.println("Space-Schedule association added successfully.");
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }
    }

    public void scheduleToInstructor(Schedule schedule, Instructor instructor) {
        String insertSchInsSql =
                "INSERT INTO lessons (instructor_id, schedule_id, availability) VALUES (?, ?, 'available')";
        try (Connection connection = getConnection()) {
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSchInsSql)) {
                insertStmt.setInt(1, instructor.getId());
                insertStmt.setInt(2, schedule.getId());
                insertStmt.executeUpdate();
                System.out.println("Offering-Instructor association added successfully.");
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }
    }

    public Map<String, List<Schedule>> groupSchedulesByDateRange(List<Schedule> schedules) {
        Map<String, List<Schedule>> schedulesByDateRange = new HashMap<>();

        for (Schedule schedule : schedules) {
            String dateRange = schedule.getStartDate() + " - " + schedule.getEndDate();
            schedulesByDateRange.computeIfAbsent(dateRange, k -> new ArrayList<>());
            schedulesByDateRange.get(dateRange).add(schedule);
        }

        return schedulesByDateRange;
    }

    public void printGroupedSchedules(Map<String, List<Schedule>> groupedSchedules,
            Offering offering, OfferingDAO offeringDAO, City city, Location location, Space space,
            List<Lesson> lessons) {

        for (Map.Entry<String, List<Schedule>> entry : groupedSchedules.entrySet()) {
            String dateRange = entry.getKey();
            List<Schedule> schedules = entry.getValue();

            if (lessons.isEmpty()) {
                // Print offerings for date ranges without lessons
                System.out.println("Offerings for date range: " + dateRange);

                for (Schedule schedule : schedules) {
                    offering = offeringDAO.fetchFromDb(String
                            .valueOf(offeringDAO.getOfferingIdByScheduleId(schedule.getId())));
                    if (offering == null) {
                        System.out.println("Offering not found.");
                        return;
                    }
                    System.out.println("ID: " + schedule.getId() + ". We offer a "
                            + offering.getType().toString().toLowerCase() + " "
                            + offering.getCourseName() + " course in " + city.getCityName() + " in "
                            + location.getLocationName() + " at " + space.getSpaceName() + ".");
                    System.out.println("  - Day of Week: " + schedule.getDayOfWeek()
                            + ", Start Time: " + schedule.getStartTime() + ", End Time: "
                            + schedule.getEndTime());
                }
                System.out.println();

            } else {
                // Print lessons associated with specific date ranges
                System.out.println("Lessons for date range: " + dateRange);

                for (Lesson lesson : lessons) {
                    Schedule lessonSchedule = lesson.getSchedule();
                    String lessonDateRange =
                            lessonSchedule.getStartDate() + " - " + lessonSchedule.getEndDate();

                    // Check if lesson’s schedule falls within the current date range
                    if (lessonDateRange.equals(dateRange)) {
                        offering = offeringDAO.fetchFromDb(String.valueOf(
                                offeringDAO.getOfferingIdByScheduleId(lessonSchedule.getId())));
                        System.out.println("ID: " + lesson.getId() + ". "
                                + lesson.getAvailable().toUpperCase() + "- We offer a "
                                + offering.getType().toString().toLowerCase() + " "
                                + offering.getCourseName() + " course taught by "
                                + lesson.getInstructor().getIdentity() + " in " + city.getCityName()
                                + " in " + location.getLocationName() + " at "
                                + space.getSpaceName() + ".");
                        System.out.println("  - Day of Week: " + lessonSchedule.getDayOfWeek()
                                + ", Start Time: " + lessonSchedule.getStartTime() + ", End Time: "
                                + lessonSchedule.getEndTime());
                    }
                }
                System.out.println();
            }
        }
    }


    @Override
    public Schedule fetchFromDb(String id) {
        String sql = "SELECT id, start_date, end_date, day_of_week, start_time, end_time "
                + "FROM schedules WHERE id = ?";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int scheduleId = resultSet.getInt("id");
                String startDate = resultSet.getDate("start_date").toString();
                String endDate = resultSet.getDate("end_date").toString();
                String dayOfWeek = resultSet.getString("day_of_week");
                String startTime = resultSet.getTime("start_time").toString();
                String endTime = resultSet.getTime("end_time").toString();

                return new Schedule(scheduleId, startDate, endDate, EDayOfWeek.from(dayOfWeek),
                        startTime, endTime);
            } else {
                System.out.println("Schedule not found.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
            return null;
        }
    }


    public List<Schedule> fetchAllFromDb() {
        String sql = "SELECT id, start_date, end_date, day_of_week, start_time, end_time "
                + "FROM schedules";
        List<Schedule> schedules = new ArrayList<>();

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int scheduleId = resultSet.getInt("id");
                String startDate = resultSet.getDate("start_date").toString();
                String endDate = resultSet.getDate("end_date").toString();
                String dayOfWeek = resultSet.getString("day_of_week");
                String startTime = resultSet.getTime("start_time").toString();
                String endTime = resultSet.getTime("end_time").toString();

                schedules.add(new Schedule(scheduleId, startDate, endDate,
                        EDayOfWeek.from(dayOfWeek), startTime, endTime));
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }

        return schedules;
    }

    @Override
    public void updateDb(Schedule schedule) {
        String updateScheduleSql =
                "UPDATE schedules SET start_date = ?, end_date = ?, day_of_week = ?, start_time = ?, end_time = ? WHERE id = ?";

        try (Connection connection = getConnection()) {
            try (PreparedStatement updateStmt = connection.prepareStatement(updateScheduleSql)) {
                updateStmt.setDate(1, java.sql.Date.valueOf(schedule.getStartDate()));
                updateStmt.setDate(2, java.sql.Date.valueOf(schedule.getEndDate()));
                updateStmt.setString(3, schedule.getDayOfWeek().name());
                updateStmt.setTime(4, java.sql.Time.valueOf(schedule.getStartTime() + ":00"));
                updateStmt.setTime(5, java.sql.Time.valueOf(schedule.getEndTime() + ":00"));
                updateStmt.setInt(6, schedule.getId());

                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Schedule updated successfully.");
                } else {
                    System.out.println("Schedule not found.");
                }
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }
    }

    @Override
    public void removeFromDb(Schedule schedule) {
        String deleteScheduleSql = "DELETE FROM schedules WHERE id = ?";

        try (Connection connection = getConnection()) {
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteScheduleSql)) {
                deleteStmt.setInt(1, schedule.getId());
                int rowsAffected = deleteStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Schedule deleted successfully.");
                } else {
                    System.out.println("Schedule not found.");
                }
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }
    }

    public City getCityFromSchedule(Schedule schedule) {
        String sql =
                "SELECT city_id, city_name FROM cities JOIN locations ON cities.id = locations.city_id "
                        + "JOIN spaces ON locations.id = spaces.location_id JOIN spaces_schedules ON spaces.id = spaces_schedules.space_id "
                        + "JOIN schedules ON spaces_schedules.schedule_id = schedules.id WHERE schedules.id = ?";
        City city = null;

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, schedule.getId());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int cityId = resultSet.getInt("city_id");
                String cityName = resultSet.getString("city_name");
                city = new City(cityId, cityName);
            }

            return city;

        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
            return null;
        }
    }
}
