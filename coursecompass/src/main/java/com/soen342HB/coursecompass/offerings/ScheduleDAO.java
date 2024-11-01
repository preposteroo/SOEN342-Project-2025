package com.soen342HB.coursecompass.offerings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.soen342HB.coursecompass.core.BaseDAO;
import com.soen342HB.coursecompass.spaces.Space;

public class ScheduleDAO extends BaseDAO<Schedule> {
    // TEMPORARY DB
    List<Schedule> schedules = new ArrayList<Schedule>();

    public ScheduleDAO() {}

    @Override
    public void addtoDb(Schedule schedule) {
        String insertScheduleSql =
                "INSERT INTO schedules (start_date, end_date, day_of_week, start_time, end_time) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection()) {
            try (PreparedStatement insertStmt = connection.prepareStatement(insertScheduleSql,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                // Set the parameters
                insertStmt.setDate(1, java.sql.Date.valueOf(schedule.getStartDate()));
                insertStmt.setDate(2, java.sql.Date.valueOf(schedule.getEndDate()));
                insertStmt.setString(3, schedule.getDayOfWeek().name());
                insertStmt.setTime(4, java.sql.Time.valueOf(schedule.getStartTime()));
                insertStmt.setTime(5, java.sql.Time.valueOf(schedule.getEndTime()));

                // Execute the update
                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    // Retrieve the generated keys
                    try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            // Get the generated ID and set it to the Schedule object
                            int newId = generatedKeys.getInt(1);
                            schedule.setId(newId); // Assuming you have a setId method in Schedule
                                                   // class
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

    @Override
    public Schedule fetchFromDb(String id) {
        for (Schedule schedule : schedules) {
            String stringId = String.valueOf(schedule.getId());
            if (stringId.equals(id)) {
                return schedule;
            }
        }
        return null;
    }

    public Schedule[] fetchAllFromDb() {
        return schedules.toArray(new Schedule[schedules.size()]);
    }

    @Override
    public void updateDb(Schedule schedule) {
        for (Schedule s : schedules) {
            if (s.getId() == schedule.getId()) {
            }
        }
    }

    @Override
    public void removeFromDb(Schedule schedule) {
        schedules.remove(schedule);
    }

}
