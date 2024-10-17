package com.soen342HB.coursecompass;

import java.util.Scanner;

public class App {
	public static void main(String[] args) {
		System.out.println("==============================");
		System.out.println("|| Welcome to CourseCompass! ||");
		System.out.println("==============================");
		System.out.println("Become an Instructor: Enter I\nSign In for Instructors: Enter SI");
		Scanner userInputScanner = new Scanner(System.in);
		System.out.println("Enter Decision: ");
		String input = userInputScanner.next();

		switch (input) {
			case "I":
				System.out.println(
						"Thank you for your interest in becoming a CourseCompass Instructor!");
				InstructorDAO instructorDAO = new InstructorDAO();
				System.out.print("Please enter your Name: ");
				String username = userInputScanner.next();
				System.out.print("Please enter your Phone Number: ");
				String phoneNumber = userInputScanner.next();
				System.out.print("Please enter your Specialization: ");
				String specialization = userInputScanner.next();
				System.out.print(
						"Please enter the Cities for which you are available (separated by commas): ");
				String cities = userInputScanner.next();

				String[] instructor_locations = cities.toLowerCase().split(",");
				Instructor instructor =
						new Instructor(username, phoneNumber, specialization, instructor_locations);

				instructorDAO.addInstructortoDb(instructor);

			case "SI":
				System.out.println("Welcome Back to CourseCompass!");
				System.out.println("Please enter your Name: ");
				System.out.println("Please enter your Phone Number: ");
			default:
				System.out.println("Invalid Input. Please try again.");
		}
		userInputScanner.close();

	}

}
