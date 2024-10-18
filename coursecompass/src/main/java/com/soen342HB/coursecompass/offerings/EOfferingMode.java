package com.soen342HB.coursecompass.offerings;

public enum EOfferingMode {
    INDIVIDUAL, GROUP;

    public static EOfferingMode from(String str) {
        switch (str.toUpperCase()) {
            case "INDIVIDUAL":
                return INDIVIDUAL;
            case "GROUP":
                return GROUP;
            default:
                return null;
        }
    }
}
