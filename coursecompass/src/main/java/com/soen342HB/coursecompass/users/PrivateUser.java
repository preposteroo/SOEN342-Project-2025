package com.soen342HB.coursecompass.users;

import java.util.ArrayList;
import com.soen342HB.coursecompass.App;

public abstract class PrivateUser extends BaseUser {

    public void executeCommand(String[] args) {
        switch (args[0]) {
            case "logout":
                logout();
                break;
            default:
                super.executeCommand(args);
        }
    }

    public ArrayList<String> getCommands() {
        ArrayList<String> list = super.getCommands();
        list.add("logout");
        return list;
    }

    public void logout() {
        System.out.println("Logging out...");
        App.setUser(new PublicUser());
        System.out.println("You are now logged in as " + App.getUser().getIdentity());
    }
}
