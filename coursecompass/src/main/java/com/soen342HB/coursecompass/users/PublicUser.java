package com.soen342HB.coursecompass.users;

import java.util.Set;
import com.soen342HB.coursecompass.App;
import com.soen342HB.coursecompass.utils.InputManager;

public class PublicUser extends BaseUser {

    public PublicUser() {}

    @Override
    public Set<String> getCommands() {
        Set<String> list = super.getCommands();
        list.add("login");
        list.add("register");
        return list;
    }

    @Override
    public void executeCommand(String[] args) {
        switch (args[0]) {
            case "login":
                login();
                break;
            case "register":
                register();
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
        String password = InputManager.getInput();
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
                StudentDAO studentDAO = new StudentDAO();
                Student student = studentDAO.fetchFromDb(username);
                if (student != null && student.getPassword().equals(password)) {
                    System.out.println("Welcome, " + username + "!");
                    App.setUser(student);
                } else {
                    System.out.println("Invalid username or password");
                }
                break;
            default:
                System.out.println("Invalid user type");
        }
        System.out.println("You are now logged in as " + App.getUser().getIdentity());
    }

    private void register() {
        System.out.print("Username: ");
        String username = InputManager.getInput();
        System.out.print("Password: ");
        String password = InputManager.getInput();
        System.out.print("Confirm password: ");
        String confirmPassword = InputManager.getInput();
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match. Operation failed.");
            return;
        }
        Student student = new Student(username, password);
        StudentDAO studentDAO = new StudentDAO();
        studentDAO.addtoDb(student);
    }
}
