package com.soen342HB.coursecompass.spaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.soen342HB.coursecompass.core.BaseDAO;

public class CityDAO extends BaseDAO<City> {

    @Override
    public void addtoDb(City city) {
        String insertCitySql = "INSERT INTO city (city_name) VALUES (?)";
        try (Connection connection = getConnection()) {
            try (PreparedStatement insertStmt = connection.prepareStatement(insertCitySql,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, city.getCityName());
                insertStmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }

    }

    @Override
    public void removeFromDb(City city) {
        String deleteCitySql = "DELETE FROM city WHERE id = ?";
        try (Connection connection = getConnection()) {
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteCitySql)) {
                deleteStmt.setInt(1, city.getId());
                deleteStmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }
    }

    @Override
    public City fetchFromDb(String id) {
        String selectCitySql = "SELECT id, city_name FROM city WHERE id = ?";
        City city = null;

        try (Connection connection = getConnection();
                PreparedStatement selectStmt = connection.prepareStatement(selectCitySql)) {

            selectStmt.setString(1, id);

            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    city = new City(rs.getInt("id"), rs.getString("city_name"));
                } else {
                    System.out.println("No city found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }

        return city;
    }

    public City fetchFromDbByName(String cityName) {
        String selectCitySql = "SELECT id, city_name FROM city WHERE city_name = ?";
        City city = null;

        try (Connection connection = getConnection();
                PreparedStatement selectStmt = connection.prepareStatement(selectCitySql)) {

            selectStmt.setString(1, cityName);

            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    city = new City(rs.getInt("id"), rs.getString("city_name"));
                } else {
                    System.out.println("No city found with name: " + cityName);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }

        return city;
    }


    public City[] fetchAllFromDb() {
        String selectCityNamesSql = "SELECT id, city_name FROM city";
        List<City> cities = new ArrayList<>();

        try (Connection connection = getConnection()) {
            try (PreparedStatement selectStmt = connection.prepareStatement(selectCityNamesSql)) {
                try (ResultSet rs = selectStmt.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String cityName = rs.getString("city_name");
                        cities.add(new City(id, cityName));
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
        return cities.toArray(new City[0]);
    }

    @Override
    public void updateDb(City city) {
        String updateCitySql = "UPDATE city SET city_name = ? WHERE id = ?";
        try (Connection connection = getConnection()) {
            try (PreparedStatement updateStmt = connection.prepareStatement(updateCitySql)) {
                updateStmt.setString(1, city.getCityName());
                updateStmt.setInt(2, city.getId());
                updateStmt.executeUpdate();
                System.out.println("City updated successfully.");
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }
    }

    public void locationToCity(Location location, City city) {
        String insertCityLocSql =
                "INSERT INTO cities_location (city_id, location_id) VALUES (?, ?)";
        try (Connection connection = getConnection()) {
            try (PreparedStatement insertStmt = connection.prepareStatement(insertCityLocSql)) {
                insertStmt.setInt(1, city.getId());
                insertStmt.setInt(2, location.getId());
                insertStmt.executeUpdate();
                System.out.println("City-location association added successfully.");
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection error occurred: " + e.getMessage());
        }
    }

    public ArrayList<Location> getLocationsForCity(City city) {
        String selectLocationsSql = "SELECT l.id, l.location_name " + "FROM cities_location cl "
                + "JOIN location l ON cl.location_id = l.id " + "WHERE cl.city_id = ?";
        ArrayList<Location> locations = new ArrayList<>();

        try (Connection connection = getConnection()) {
            try (PreparedStatement selectStmt = connection.prepareStatement(selectLocationsSql)) {
                selectStmt.setInt(1, city.getId());

                try (ResultSet rs = selectStmt.executeQuery()) {
                    while (rs.next()) {
                        int locationId = rs.getInt("id");
                        String locationName = rs.getString("location_name");
                        locations.add(new Location(locationId, locationName));
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

        return locations;
    }
}
