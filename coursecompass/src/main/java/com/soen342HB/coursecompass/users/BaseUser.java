package com.soen342HB.coursecompass.users;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import com.soen342HB.coursecompass.App;
import com.soen342HB.coursecompass.offerings.Lesson;
import com.soen342HB.coursecompass.offerings.LessonDAO;

public abstract class BaseUser {

    public void executeCommand(String[] args) {
        switch (args[0]) {
            case "exit":
                App.exit();
                break;
            case "help":
                help();
                break;
            case "":
                break;
            case "lessons":
                lessons();
                break;
            default:
                System.out.println("Invalid command.");
        }
    }

    public Set<String> getCommands() {
        Set<String> list = new HashSet<String>();
        list.add("help");
        list.add("exit");
        list.add("offerings");
        return list;
    }

    public void help() {
        System.out.println("Available commands:");
        for (String command : getCommands()) {
            System.out.println("  " + command);
        }
    }

    public void lessons() {
        System.out.println("Lessons:");
        LessonDAO lessonDAO = new LessonDAO();
        List<Lesson> lessons = lessonDAO.fetchAllFromDb();
        for (Lesson lesson : lessons) {
            System.out.println("  " + lesson);
        }
    }

    public abstract String getIdentity();
}
