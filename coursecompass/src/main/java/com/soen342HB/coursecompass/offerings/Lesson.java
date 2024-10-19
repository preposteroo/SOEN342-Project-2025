package com.soen342HB.coursecompass.offerings;

import com.soen342HB.coursecompass.users.Instructor;

public class Lesson {
    private Offering offering;
    private Instructor instructor;
    private String id;

    public Lesson(Offering offering, Instructor instructor, String id) {
        this.offering = offering;
        this.instructor = instructor;
        this.id = id;
    }

    public Offering getOffering() {
        return offering;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public String getId() {
        return id;
    }

    public void setOffering(Offering offering) {
        this.offering = offering;
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
        return id + " (" + offering + ", " + instructor + ")";
    }
}
