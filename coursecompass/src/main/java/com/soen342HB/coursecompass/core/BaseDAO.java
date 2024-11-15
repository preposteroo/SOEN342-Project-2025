package com.soen342HB.coursecompass.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseDAO<T> {
    private static final String URL =
            "jdbc:mysql://76.66.175.66:3300/CourseCompass_db?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "toor";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public abstract void addtoDb(T t);

    public abstract void removeFromDb(T t);

    public abstract T fetchFromDb(String id);

    public abstract void updateDb(T t);
}

