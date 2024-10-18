package com.soen342HB.coursecompass;

import com.soen342HB.coursecompass.users.BaseUser;
import com.soen342HB.coursecompass.users.PublicUser;
import com.soen342HB.coursecompass.utils.InputManager;

public class App {

	private static BaseUser user;
	private static boolean running = true;

	public static void setUser(BaseUser user) {
		App.user = user;
	}

	public static BaseUser getUser() {
		return user;
	}

	public static void exit() {
		InputManager.close();
		running = false;
	}

	public static void main(String[] args) {
		user = new PublicUser();
		System.out.println("==============================");
		System.out.println("|| Welcome to CourseCompass! ||");
		System.out.println("==============================");
		System.out.println("Type 'help' for a list of commands");
		System.out.println("Type 'exit' to quit");
		while (running) {
			System.out.print("> ");
			String command = InputManager.getInput();
			user.executeCommand(command.split(" "));
		}
		System.out.println("Goodbye!");
	}
}
