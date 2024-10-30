package com.soen342HB.coursecompass.offerings;

public class Offering {
    private int id;
    private EOfferingMode type;
    private String courseName;
    private Schedule schedule;

    public Offering(EOfferingMode type, String courseName, Schedule schedule) {
        this.type = type;
        this.courseName = courseName;
        this.schedule = schedule;
    }

    public Offering(int id, EOfferingMode type, String courseName, Schedule schedule) {
        this.id = id;
        this.type = type;
        this.courseName = courseName;
        this.schedule = schedule;
    }


    public EOfferingMode getType() {
        return type;
    }

    public void setType(EOfferingMode type) {
        this.type = type;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String toString() {
        return "Offering{" + "id=" + id + ",type=" + type + ", schedule=" + schedule + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
