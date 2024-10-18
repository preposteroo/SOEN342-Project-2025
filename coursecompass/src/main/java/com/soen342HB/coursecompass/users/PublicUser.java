package com.soen342HB.coursecompass.users;

import java.util.ArrayList;
import com.soen342HB.coursecompass.App;
import com.soen342HB.coursecompass.utils.InputManager;

public class PublicUser extends BaseUser {

    public PublicUser() {}

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> list = super.getCommands();
        list.add("login");
        list.add("makeuser");
        return list;
    }

    @Override
    public void executeCommand(String[] args) {
        switch (args[0]) {
            case "login":
                login();
                break;
            case "makeuser":
                makeuser();
                break;
            default:
                super.executeCommand(args);
        }
    }

    @Override
    public String getIdentity() {
        return "public user";
    }

    private void login() {
        System.out.println("Are you an administrator, an instructor or a student?");
        System.out.print("Type 'admin', 'instructor' or 'student': ");
        String userType = InputManager.getInput();
        System.out.print("Username: ");
        String username = InputManager.getInput();
        System.out.print("Password: ");
        String password = InputManager.getPassword();
        switch (userType) {
            case "admin":
                AdministratorDAO adminDAO = new AdministratorDAO();
                Administrator admin = adminDAO.fetchFromDb(username);
                if (admin != null && admin.getPassword().equals(password)) {
                    System.out.println("Welcome, " + username + "!");
                    App.setUser(admin);
                } else {
                    System.out.println("Invalid username or password");
                }
                break;
            case "instructor":
                InstructorDAO instructorDAO = new InstructorDAO();
                Instructor instructor = instructorDAO.fetchFromDb(username);
                if (instructor != null && instructor.getPassword().equals(password)) {
                    System.out.println("Welcome, " + username + "!");
                    App.setUser(instructor);
                } else {
                    System.out.println("Invalid username or password");
                }
                break;
            case "student":
                // TODO: Implement student login
                break;
            default:
                System.out.println("Invalid user type");
        }
        System.out.println("You are now logged in as " + App.getUser().getIdentity());
    }

    private void makeuser() {
        System.out.println("Are you an administrator, an instructor or a student?");
        System.out.print("Type 'admin', 'instructor' or 'student': ");
        String userType = InputManager.getInput();
        System.out.print("Username: ");
        String username = InputManager.getInput();
        System.out.print("Password: ");
        String password = InputManager.getPassword();
        System.out.print("Confirm password: ");
        String confirmPassword = InputManager.getPassword();
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match. Operation failed.");
            return;
        }
        switch (userType) {
            case "admin":
                Administrator admin = new Administrator(username, password);
                AdministratorDAO adminDAO = new AdministratorDAO();
                adminDAO.addtoDb(admin);
                break;
            case "instructor":
                System.out.print("Specialization: ");
                String specialization = InputManager.getInput();
                Instructor instructor = new Instructor(username, password, specialization);
                InstructorDAO instructorDAO = new InstructorDAO();
                instructorDAO.addtoDb(instructor);
                break;
            case "student":
                // Implement student creation
                break;
            default:
                System.out.println("Invalid user type");
                return;
        }
    }
}
