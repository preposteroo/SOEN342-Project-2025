package com.soen342HB.coursecompass.spaces;

import com.soen342HB.coursecompass.offerings.Schedule;
import java.util.ArrayList;

public class Space {
    private int id;
    private String spaceName;
    ArrayList<Schedule> schedules;

    public Space(String spaceName) {
        this.spaceName = spaceName;
    }

    public Space(int id, String spaceName) {
        this.id = id;
        this.spaceName = spaceName;
    }


    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(ArrayList<Schedule> schedules) {
        this.schedules = schedules;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
