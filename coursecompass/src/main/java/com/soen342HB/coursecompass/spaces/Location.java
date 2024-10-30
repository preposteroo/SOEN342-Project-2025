package com.soen342HB.coursecompass.spaces;

import java.util.ArrayList;

public class Location {
    private int id;
    private String locationName;
    private ArrayList<Space> spaces;

    public Location(String locationName) {
        this.locationName = locationName;
    }

    public Location(int id, String locationName) {
        this.id = id;
        this.locationName = locationName;
    }


    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Space> getSpaces() {
        return spaces;
    }

    public void setSpaces(ArrayList<Space> spaces) {
        this.spaces = spaces;
    }



}
