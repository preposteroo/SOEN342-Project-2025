package com.soen342HB.coursecompass;

public class Instructor {
	private static String username;
	private static String password; //Phone Number
	private static String specialization;
	private static String[] cityNames;
	
	public Instructor(){
		
	}
	public Instructor (String username,String password,String specialization,String[] cityNames) {
		Instructor.username=username;
		Instructor.password=password;
		Instructor.specialization=specialization;
		Instructor.cityNames=cityNames;
	}
	public static String getUsername() {
		return username;
	}
	public static void setUsername(String username) {
		Instructor.username = username;
	}
	public static String getPassword() {
		return password;
	}
	public static void setPassword(String password) {
		Instructor.password = password;
	}
	public static String getSpecialization() {
		return specialization;
	}
	public static void setSpecialization(String specialization) {
		Instructor.specialization = specialization;
	}
	public static String[] getCityNames() {
		return cityNames;
	}
	public static void setCityNames(String[] cityNames) {
		Instructor.cityNames = cityNames;
	}
	
	
}
