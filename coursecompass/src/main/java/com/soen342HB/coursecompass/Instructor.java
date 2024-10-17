package com.soen342HB.coursecompass;

public class Instructor {
	private static String username;
	private static String password; // Phone Number
	private static String specialization;
	private static String[] cityNames;

	public Instructor() {

	}

	public Instructor(String username, String password, String specialization, String[] cityNames) {
		Instructor.username = username;
		Instructor.password = password;
		Instructor.specialization = specialization;
		Instructor.cityNames = cityNames;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		Instructor.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public String[] getCityNames() {
		return cityNames;
	}

	public void setCityNames(String[] cityNames) {
		this.cityNames = cityNames;
	}


}
