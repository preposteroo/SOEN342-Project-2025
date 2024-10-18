package com.soen342HB.coursecompass.users;

import java.util.Set;
import java.util.HashSet;
import com.soen342HB.coursecompass.App;

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
            default:
                System.out.println("Invalid command.");
        }
    }

    public Set<String> getCommands() {
        var list = new HashSet<String>();
        list.add("help");
        list.add("exit");
        return list;
    }

    public void help() {
        System.out.println("Available commands:");
        for (String command : getCommands()) {
            System.out.println("  " + command);
        }
    }

    public abstract String getIdentity();
}
