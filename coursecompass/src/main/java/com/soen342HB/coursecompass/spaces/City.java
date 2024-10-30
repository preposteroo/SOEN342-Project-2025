package com.soen342HB.coursecompass.spaces;

import java.util.ArrayList;

public class City {
    private String cityName;
    private int id;
    private ArrayList<Location> locations;

    public City(String cityName) {
        this.cityName = cityName;
    }


    public City(int id, String cityName) {
        this.id = id;
        this.cityName = cityName;

    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }

}
