package com.soen342HB.coursecompass;

public class Instructor {
	private String username;
	private String password; // Phone Number
	private String specialization;
	private String[] cityNames;

	public Instructor(String username, String password, String specialization, String[] cityNames) {
		this.username = username;
		this.password = password;
		this.specialization = specialization;
		this.cityNames = cityNames;
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
