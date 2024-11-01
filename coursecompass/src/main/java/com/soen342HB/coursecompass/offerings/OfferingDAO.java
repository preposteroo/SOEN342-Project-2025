package com.soen342HB.coursecompass.offerings;

import com.soen342HB.coursecompass.core.BaseDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OfferingDAO extends BaseDAO<Offering> {
    // temp DB
    public static ArrayList<Offering> db = new ArrayList<Offering>();

    @Override
    public void addtoDb(Offering offering) {
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
    }


    public void offeringToSchedule(Offering offering, Schedule schedule) {
        String insertOffScheSql =
                "INSERT INTO schedule_offering (schedule_id, offering_id) VALUES (?, ?)";
        try (Connection connection = getConnection()) {
            try (PreparedStatement insertStmt = connection.prepareStatement(insertOffScheSql)) {
                insertStmt.setInt(1, schedule.getId());
                insertStmt.setInt(2, offering.getId());
                insertStmt.executeUpdate();
                System.out.println("Schedule-Offering association added successfully.");
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }
    }

    @Override
    public void removeFromDb(Offering offering) {
        db.remove(offering);
    }

    @Override
    public Offering fetchFromDb(String id) {
        for (Offering offering : db) {
            String stringId = String.valueOf(offering.getId());
            if (stringId.equals(id)) {
                return offering;
            }
        }
        return null;
    }

    public Offering[] fetchAllFromDb() {
        Offering[] offerings = new Offering[db.size()];
        for (int i = 0; i < db.size(); i++) {
            offerings[i] = db.get(i);
        }
        return offerings;
    }

    @Override
    public void updateDb(Offering offering) {
        for (Offering o : db) {
            if (o.getSchedule().getSpace().getSpaceName()
                    .equals(offering.getSchedule().getSpace().getSpaceName())) {
                o.setType(offering.getType());
                o.setSchedule(offering.getSchedule());
            }
        }
    }
}
