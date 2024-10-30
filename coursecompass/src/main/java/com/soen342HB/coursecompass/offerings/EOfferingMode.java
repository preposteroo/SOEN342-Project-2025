package com.soen342HB.coursecompass.offerings;

public enum EOfferingMode {
    PRIVATE, GROUP;

    public static EOfferingMode from(String str) {
        switch (str.toUpperCase()) {
            case "PRIVATE":
                return PRIVATE;
            case "GROUP":
                return GROUP;
            default:
                return null;
        }
    }
}
