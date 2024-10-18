package com.soen342HB.coursecompass.users;

import java.util.Set;
import com.soen342HB.coursecompass.offerings.EDayOfWeek;
import com.soen342HB.coursecompass.offerings.EOfferingMode;
import com.soen342HB.coursecompass.offerings.Offering;
import com.soen342HB.coursecompass.offerings.OfferingDAO;
import com.soen342HB.coursecompass.offerings.Schedule;
import com.soen342HB.coursecompass.spaces.Space;
import com.soen342HB.coursecompass.utils.InputManager;

public class Administrator extends PrivateUser {
    private String username;
    private String password;

    @Override
    public void executeCommand(String[] args) {
        switch (args[0]) {
            case "offerings":
                offerings(args);
                break;
            default:
                super.executeCommand(args);
        }
    }

    @Override
    public Set<String> getCommands() {
        Set<String> list = super.getCommands();
        list.add("offerings");
        return list;
    }

    private void offerings(String[] flags) {
        if (flags.length < 2) {
            System.out.println("Invalid use of the command offerings.");
            return;
        }
        switch (flags[1]) {
            case "create":
                System.out.println("Creating an offering...");
                System.out.println("The course mode can be 'group' or 'individual'.");
                System.out.print("Enter the course mode: ");
                EOfferingMode courseType = EOfferingMode.from(InputManager.getInput());
                System.out.print("Enter the start date: ");
                String startDate = InputManager.getInput();
                System.out.print("Enter the end date: ");
                String endDate = InputManager.getInput();
                System.out.print("Enter the day of the week: ");
                String dayOfWeek = InputManager.getInput();
                System.out.print("Enter the start time: ");
                String startTime = InputManager.getInput();
                System.out.print("Enter the end time: ");
                String endTime = InputManager.getInput();
                System.out.print("Enter the space name: ");
                String spacename = InputManager.getInput();
                Space space = new Space(spacename);
                Schedule schedule = new Schedule(startDate, endDate, EDayOfWeek.from(dayOfWeek),
                        startTime, endTime, space);
                Offering offering = new Offering(courseType, schedule);
                OfferingDAO offeringDAO = new OfferingDAO();
                offeringDAO.addtoDb(offering);
                break;
            default:
                System.out.println("Invalid use of the command offerings.");
        }
    }

    public Administrator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getIdentity() {
        return "Administrator " + username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
