package com.soen342HB.coursecompass;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
		System.out.println("==============================");
		System.out.println("|| Welcome to CourseCompass! ||");
		System.out.println("==============================");
		System.out.println("Become an Instructor: Enter I"+"\n"
							+"Sign In for Instructors: Enter SI"+"\n"
							+"Sign In for Admin: Enter SI");
		Scanner myObj = new Scanner(System.in);
		System.out.println("Enter Decision: ");
		String input = myObj.nextLine();
		
		switch(input) {
		case "I":
			System.out.println("Thank you for your interest in becoming a CourseCompass Instructor!");
			InstructorDAO instructorDAORegistration = new InstructorDAO();
			System.out.print("Please enter your Name: ");
			String username = myObj.next();
			System.out.print("Please enter your Phone Number: ");
			String phoneNumber = myObj.next();
			System.out.print("Please enter your Specialization: ");
			String specialization = myObj.next();
			System.out.print("Please enter the Cities for which you are available (separated by commas): ");
			String cities = myObj.next();
			
			String[] instructor_locations = cities.toLowerCase().split(",");
			Instructor instructor = new Instructor(username,phoneNumber,specialization,instructor_locations);
	        
			instructorDAORegistration.addInstructortoDb(instructor);
			break;
			
		case "SI":
			InstructorDAO instructorDAOSignIn = new InstructorDAO();
			System.out.println("Welcome Back to CourseCompass!");
			System.out.println("Please enter your Name: ");
			String username_authentication = myObj.next();
			System.out.println("Please enter your Phone Number: ");
			String password_authentication = myObj.next();
			instructorDAOSignIn.authenticateInstructor(username_authentication, password_authentication);

		}
		
    }
		
}
