package com.soen342HB.coursecompass.offerings;

import com.soen342HB.coursecompass.core.BaseDAO;
import com.soen342HB.coursecompass.users.Instructor;
import com.soen342HB.coursecompass.spaces.City;
import com.soen342HB.coursecompass.spaces.CityDAO;
import com.soen342HB.coursecompass.spaces.Location;
import com.soen342HB.coursecompass.spaces.LocationDAO;
import com.soen342HB.coursecompass.spaces.SpaceDAO;
import com.soen342HB.coursecompass.spaces.Space;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OfferingDAO extends BaseDAO<Offering> {

    @Override
    public void addtoDb(Offering offering) {
        try {
            startWrite();
        } catch (Exception e) {
            System.out.println("Error occurred while acquiring write lock: " + e.getMessage());
            return;
        }
        String insertOfferingSql =
                "INSERT INTO offerings (course_name, offering_mode) VALUES (?, ?)";

        try (Connection connection = getConnection()) {
            try (PreparedStatement insertStmt = connection.prepareStatement(insertOfferingSql,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                // Set the parameters
                insertStmt.setString(1, offering.getCourseName());
                insertStmt.setString(2, offering.getType().name()); // Ensure you're setting the
                                                                    // second parameter correctly
                // Execute the update
                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    // Retrieve the generated keys
                    try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            // Get the generated ID and set it to the Offering object
                            int newId = generatedKeys.getInt(1);
                            offering.setId(newId); // Assuming you have a setId method in the
                                                   // Offering class
                            System.out.println("Offering added successfully with ID: " + newId);
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }

        endWrite();
    }

    public void offeringToInstructor(Offering offering, Instructor instructor) {
        String insertOffInsSql =
                "INSERT INTO lessons (instructor_id, offering_id, availability) VALUES (?, ?, 'available')";
        try (Connection connection = getConnection()) {
            try (PreparedStatement insertStmt = connection.prepareStatement(insertOffInsSql)) {
                insertStmt.setInt(1, instructor.getId());
                insertStmt.setInt(2, offering.getId());
                insertStmt.executeUpdate();
                System.out.println("Offering-Instructor association added successfully.");
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }
    }

    @Override
    public void removeFromDb(Offering offering) {
        String sql = "DELETE FROM offerings WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, offering.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    @Override
    public Offering fetchFromDb(String id) {
        String selectOfferingSql =
                "SELECT id, course_name, offering_mode FROM offerings WHERE id = ?";
        Offering offering = null;

        try (Connection connection = getConnection();
                PreparedStatement selectStmt = connection.prepareStatement(selectOfferingSql)) {

            selectStmt.setString(1, id);

            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    offering = new Offering(rs.getInt("id"),
                            EOfferingMode.from(rs.getString("offering_mode")),
                            rs.getString("course_name"));
                } else {
                    // System.out.println("No offering found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }

        return offering;
    }

    public int getOfferingIdByScheduleId(int scheduleId) {
        int offeringId = -1;
        String query = "SELECT offering_id FROM schedules WHERE id = ?";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, scheduleId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                offeringId = resultSet.getInt("offering_id");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving offering ID: " + e.getMessage());
        }

        return offeringId;
    }


    public void fetchAllFromDb() {
        String sql = "SELECT id, course_name, offering_mode FROM offerings";
        ArrayList<Offering> offerings = new ArrayList<>();
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String courseName = resultSet.getString("course_name");
                EOfferingMode offeringMode =
                        EOfferingMode.from(resultSet.getString("offering_mode"));
                Offering offering = new Offering(id, offeringMode, courseName);
                offerings.add(offering);
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }
        if (offerings.isEmpty()) {
            System.out.println("No offerings found.");
            return;
        }

        CityDAO cityDAO = new CityDAO();
        LocationDAO locationDAO = new LocationDAO();
        SpaceDAO spaceDAO = new SpaceDAO();
        City[] cities = cityDAO.fetchAllFromDb();
        for (City city : cities) {
            city.setLocations(cityDAO.getLocationsForCity(city));
            if (city.getLocations().isEmpty()) {
                continue;
            }
            System.out.println("Offerings in " + city.getCityName());
            for (Location location : city.getLocations()) {
                location.setSpaces(locationDAO.getSpacesForLocation(location));
                for (Space space : location.getSpaces()) {
                    space.setSchedules(spaceDAO.getSchedulesForSpace(space));
                    for (Schedule schedule : space.getSchedules()) {
                        Offering offering = fetchFromDb(
                                String.valueOf(getOfferingIdByScheduleId(schedule.getId())));
                        if (offering != null) {
                            System.out.println(offering.getId() + " " + location.getLocationName()
                                    + " " + space.getSpaceName() + " " + schedule.getStartDate()
                                    + " " + schedule.getEndDate() + " " + schedule.getStartTime()
                                    + " " + schedule.getEndTime() + " " + schedule.getDayOfWeek()
                                    + " " + offering.getCourseName() + " " + offering.getType());
                        }
                    }

                }
            }
        }

    }

    @Override
    public void updateDb(Offering offering) {
        String sql = "UPDATE offerings SET course_name = ?, offering_mode = ? WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, offering.getCourseName());
            statement.setString(2, offering.getType().name());
            statement.setInt(3, offering.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
}
