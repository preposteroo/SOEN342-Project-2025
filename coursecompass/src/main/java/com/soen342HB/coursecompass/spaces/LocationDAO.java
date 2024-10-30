package com.soen342HB.coursecompass.spaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.soen342HB.coursecompass.core.BaseDAO;

public class LocationDAO extends BaseDAO<Location> {
    // temp DB
    public static List<Location> db = new ArrayList<Location>();

    @Override
    public void addtoDb(Location location) {
        String insertLocationSql = "INSERT INTO location (location_name) VALUES (?)";
        try (Connection connection = getConnection()) {
            try (PreparedStatement insertStmt = connection.prepareStatement(insertLocationSql,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, location.getLocationName());
                insertStmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }

    }

    @Override
    public void removeFromDb(Location location) {
        db.remove(location);
    }

    @Override
    public Location fetchFromDb(String id) {
        String selectLocationSql = "SELECT id, location_name FROM location WHERE id = ?";
        Location location = null;

        try (Connection connection = getConnection();
                PreparedStatement selectStmt = connection.prepareStatement(selectLocationSql)) {

            selectStmt.setString(1, id);

            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    location = new Location(rs.getInt("id"), rs.getString("location_name"));
                } else {
                    System.out.println("No location found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }

        return location;
    }

    public Integer getLocationIdByName(String locationName) {
        String selectLocationIdSql = "SELECT id FROM location WHERE location_name = ?";
        Integer locationId = null;

        try (Connection connection = getConnection();
                PreparedStatement selectStmt = connection.prepareStatement(selectLocationIdSql)) {

            selectStmt.setString(1, locationName);

            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    locationId = rs.getInt("id");
                } else {
                    System.out.println("No location found with name: " + locationName);
                }
            } catch (SQLException e) {
                System.out.println("ResultSet error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }

        return locationId;
    }

    public void spaceToLocation(Space space, Location location) {
        String insertLocSpaceSql =
                "INSERT INTO location_spaces (location_id, spaces_id) VALUES (?, ?)";
        try (Connection connection = getConnection()) {
            try (PreparedStatement insertStmt = connection.prepareStatement(insertLocSpaceSql)) {
                insertStmt.setInt(1, location.getId());
                insertStmt.setInt(2, space.getId());
                insertStmt.executeUpdate();
                System.out.println("Location-Space association added successfully.");
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }
    }

    public ArrayList<Space> getSpacesForLocation(Location location) {
        String selectSpacesSql = "SELECT s.id, s.space_name " + "FROM location_spaces ls "
                + "JOIN spaces s ON ls.spaces_id = s.id " + "WHERE ls.location_id = ?";
        ArrayList<Space> spaces = new ArrayList<>();

        try (Connection connection = getConnection()) {
            try (PreparedStatement selectStmt = connection.prepareStatement(selectSpacesSql)) {
                selectStmt.setInt(1, location.getId());

                try (ResultSet rs = selectStmt.executeQuery()) {
                    while (rs.next()) {
                        int spaceId = rs.getInt("id");
                        String spaceName = rs.getString("space_name");
                        spaces.add(new Space(spaceId, spaceName));
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

        return spaces;
    }


    public Location[] fetchAllFromDb() {
        Location[] locations = new Location[db.size()];
        for (int i = 0; i < db.size(); i++) {
            locations[i] = db.get(i);
        }
        return locations;
    }

    @Override
    public void updateDb(Location location) {
        for (Location l : db) {
            if (l.getLocationName().equals(location.getLocationName())) {
                l.setSpaces(location.getSpaces());
            }
        }
    }

}
