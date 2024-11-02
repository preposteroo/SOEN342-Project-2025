package com.soen342HB.coursecompass.offerings;

import com.soen342HB.coursecompass.users.Instructor;

public class Lesson {
    private String id;
    private Schedule schedule;
    private Instructor instructor;
    private String available;

    public Lesson(String id, Schedule schedule, Instructor instructor, String available) {
        this.id = id;
        this.schedule = schedule;
        this.instructor = instructor;
        this.available = available;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public String getId() {
        return id;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Lesson) {
            Lesson lesson = (Lesson) obj;
            return lesson.getId().equals(this.id);
        }
        return false;
    }

    @Override
    public String toString() {
        return id + " (" + schedule + ", " + instructor + ")";
    }


    public Schedule getSchedule() {
        return schedule;
    }


    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
}
