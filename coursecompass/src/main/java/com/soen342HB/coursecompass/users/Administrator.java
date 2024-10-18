package com.soen342HB.coursecompass.users;

import java.util.ArrayList;

public class Administrator extends PrivateUser {
    private String username;
    private String password;

    @Override
    public void executeCommand(String[] args) {
        switch (args[0]) {
            case "offerings":
                offerings(args[1]);
                break;
            default:
                super.executeCommand(args);
        }
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> list = super.getCommands();
        list.add("offerings");
        return list;
    }

    private void offerings(String flag) {
        switch (flag) {
            case "create":
                System.out.println("Creating an offering...");
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
