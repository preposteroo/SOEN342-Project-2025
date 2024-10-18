package com.soen342HB.coursecompass.offerings;

public class Offering {
    // Do we need a DateTime object here?
    private EOfferingType type;
    private Schedule schedule;

    public Offering(EOfferingType type, Schedule schedule) {
        this.type = type;
        this.schedule = schedule;
    }

    public EOfferingType getType() {
        return type;
    }

    public void setType(EOfferingType type) {
        this.type = type;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
