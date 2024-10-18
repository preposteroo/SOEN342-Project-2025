package com.soen342HB.coursecompass;

public class Schedule {
    private String startDate;
    private String endDate;
    private int dayOfWeek;
    private String startTime;
    private String endTime;

    public Schedule() {}

    public Schedule(String startDate, String endDate, int dayOfWeek, String startTime,
            String endTime) {
        this.startDate = startDate;
        this.dayOfWeek = dayOfWeek;
        this.endDate = endDate;
        this.endTime = endTime;
        this.startTime = startTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


}
