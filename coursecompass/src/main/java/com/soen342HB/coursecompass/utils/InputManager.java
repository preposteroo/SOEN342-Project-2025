package com.soen342HB.coursecompass.utils;

import java.util.Scanner;
import java.io.Console;

public class InputManager {
    private static final Scanner scanner = new Scanner(System.in);

    public static String getInput() {
        return scanner.nextLine();
    }

    public static void close() {
        scanner.close();
    }

    public static String getPassword() {
        Console console = System.console();
        if (console == null) {
            System.out.println("Couldn't get Console instance");
            System.exit(0);
        }
        char passwordArray[] = console.readPassword();
        return new String(passwordArray);
    }
}
