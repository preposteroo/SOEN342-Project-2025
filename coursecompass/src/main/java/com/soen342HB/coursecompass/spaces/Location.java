package com.soen342HB.coursecompass.spaces;

import java.util.ArrayList;

public class Location {
    private String locationName;
    private ArrayList<Space> spaces;

    public Location(String locationName) {
        this.locationName = locationName;
    }


    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public ArrayList<Space> getSpaces() {
        return spaces;
    }

    public void setSpaces(ArrayList<Space> spaces) {
        this.spaces = spaces;
    }



}
