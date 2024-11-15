package com.soen342HB.coursecompass.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseDAO<T> {
    private static final String URL =
            "jdbc:mysql://76.66.175.66:3300/CourseCompass_db?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "toor";
    private static AtomicInteger readers = new AtomicInteger(0);
    private static Semaphore writerLock = new Semaphore(1);

    protected void startRead() throws InterruptedException {
        if (readers.incrementAndGet() == 1) {
            writerLock.acquire();
        }
    }

    protected void endRead() {
        if (readers.decrementAndGet() == 0) {
            writerLock.release();
        }
    }

    protected void startWrite() throws InterruptedException {
        writerLock.acquire();
    }

    protected void endWrite() {
        writerLock.release();
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public abstract void addtoDb(T t);

    public abstract void removeFromDb(T t);

    public abstract T fetchFromDb(String id);

    public abstract void updateDb(T t);
}

