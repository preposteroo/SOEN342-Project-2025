package com.soen342HB.coursecompass.offerings;

public enum EDayOfWeek {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;

    public static EDayOfWeek from(String str) {
        switch (str.toUpperCase()) {
            case "SUNDAY":
                return SUNDAY;
            case "MONDAY":
                return MONDAY;
            case "TUESDAY":
                return TUESDAY;
            case "WEDNESDAY":
                return WEDNESDAY;
            case "THURSDAY":
                return THURSDAY;
            case "FRIDAY":
                return FRIDAY;
            case "SATURDAY":
                return SATURDAY;
            default:
                return null;
        }
    }
}
