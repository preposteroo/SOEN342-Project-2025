package com.soen342HB.coursecompass.offerings;

public class Offering {
    private String id;
    // Do we need a DateTime object here?
    private EOfferingMode type;
    private Schedule schedule;

    public Offering(EOfferingMode type, Schedule schedule) {
        this.type = type;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
