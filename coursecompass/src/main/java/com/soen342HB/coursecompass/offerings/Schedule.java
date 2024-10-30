package com.soen342HB.coursecompass.offerings;

import com.soen342HB.coursecompass.spaces.Space;

public class Schedule {
    private int id;
    private String startDate;
    private String endDate;
    private EDayOfWeek dayOfWeek;
    private String startTime;
    private String endTime;
    private Space space;

    public Schedule(String startDate, String endDate, EDayOfWeek dayOfWeek, String startTime,
            String endTime, Space space) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.space = space;
    }

    public Schedule(int id, String startDate, String endDate, EDayOfWeek dayOfWeek,
            String startTime, String endTime, Space space) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.space = space;
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

    public EDayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(EDayOfWeek dayOfWeek) {
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

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public String toString() {
        return "Schedule{" + "startDate=" + startDate + ", endDate=" + endDate + ", dayOfWeek="
                + dayOfWeek + ", startTime=" + startTime + ", endTime=" + endTime + ", space="
                + space + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
