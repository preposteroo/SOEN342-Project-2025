package com.soen342HB.coursecompass.offerings;

public class Booking {
    private int id;
    private int userId;
    private int lessonId;
    private String dependentName;
    private int dependentAge;

    public Booking(int userId, int lessonId, String dependentName, int dependentAge) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.dependentName = dependentName;
        this.dependentAge = dependentAge;
    }

    public Booking(int userId, int lessonId) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.dependentName = null;
        this.dependentAge = -1;
    }


    public Booking(int id, int userId, int lessonId, String dependentName, int dependentAge) {
        this.id = id;
        this.userId = userId;
        this.lessonId = lessonId;
        this.dependentName = dependentName;
        this.dependentAge = dependentAge;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public String getDependentName() {
        return dependentName;
    }

    public void setDependentName(String dependentName) {
        this.dependentName = dependentName;
    }

    public int getDependentAge() {
        return dependentAge;
    }

    public void setDependentAge(int dependentAge) {
        this.dependentAge = dependentAge;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }



}
