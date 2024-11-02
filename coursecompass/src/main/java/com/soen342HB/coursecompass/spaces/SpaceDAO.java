package com.soen342HB.coursecompass.spaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.soen342HB.coursecompass.core.BaseDAO;
import com.soen342HB.coursecompass.offerings.EDayOfWeek;
import com.soen342HB.coursecompass.offerings.Schedule;

public class SpaceDAO extends BaseDAO<Space> {
    // temp DB
    public static List<Space> db = new ArrayList<Space>();

    @Override
    public void addtoDb(Space space) {
        String insertSpaceSql = "INSERT INTO spaces (space_name) VALUES (?)";
        try (Connection connection = getConnection()) {
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSpaceSql,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, space.getSpaceName());
                insertStmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }

    }

    @Override
    public void removeFromDb(Space space) {
        db.remove(space);
    }

    public Integer getSpaceIdByName(String spaceName) {
        String selectSpaceIdSql = "SELECT id FROM spaces WHERE space_name = ?";
        Integer spaceId = null;

        try (Connection connection = getConnection();
                PreparedStatement selectStmt = connection.prepareStatement(selectSpaceIdSql)) {

            selectStmt.setString(1, spaceName);

            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    spaceId = rs.getInt("id");
                } else {
                    System.out.println("No location found with name: " + spaceName);
                }
            } catch (SQLException e) {
                System.out.println("ResultSet error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }

        return spaceId;
    }

    @Override
    public Space fetchFromDb(String id) {
        String selectSpaceSql = "SELECT id, space_name FROM spaces WHERE id = ?";
        Space space = null;

        try (Connection connection = getConnection();
                PreparedStatement selectStmt = connection.prepareStatement(selectSpaceSql)) {

            selectStmt.setString(1, id);

            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    space = new Space(rs.getInt("id"), rs.getString("space_name"));
                } else {
                    System.out.println("No Space found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }

        return space;
    }

    public ArrayList<Schedule> getSchedulesForSpace(Space space) {
        String selectSchedulesSql =
                "SELECT sch.id, sch.start_date, sch.end_date, sch.day_of_week, sch.start_time, sch.end_time "
                        + "FROM spaces_schedules ss "
                        + "JOIN schedules sch ON ss.schedule_id = sch.id "
                        + "WHERE ss.space_id = ?";
        ArrayList<Schedule> schedules = new ArrayList<>();

        try (Connection connection = getConnection()) {
            try (PreparedStatement selectStmt = connection.prepareStatement(selectSchedulesSql)) {
                selectStmt.setInt(1, space.getId());

                try (ResultSet rs = selectStmt.executeQuery()) {
                    while (rs.next()) {
                        int scheduleId = rs.getInt("id");
                        String startDate = rs.getDate("start_date").toString();
                        String endDate = rs.getDate("end_date").toString();
                        EDayOfWeek dayOfWeek =
                                EDayOfWeek.valueOf(rs.getString("day_of_week").toUpperCase());
                        String startTime = rs.getTime("start_time").toString();
                        String endTime = rs.getTime("end_time").toString();

                        schedules.add(new Schedule(scheduleId, startDate, endDate, dayOfWeek,
                                startTime, endTime, space));
                    }
                } catch (SQLException e) {
                    System.out.println("ResultSet error occurred: " + e.getMessage());
                }
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }

        return schedules;
    }


    public Space[] fetchAllFromDb() {
        Space[] spaces = new Space[db.size()];
        for (int i = 0; i < db.size(); i++) {
            spaces[i] = db.get(i);
        }
        return spaces;
    }

    @Override
    public void updateDb(Space space) {}

}
