package com.soen342HB.coursecompass.users;

public class Instructor extends PrivateUser {
	private String username;
	private String password; // Phone Number
	private String specialization;
	private String[] cities;

	public Instructor(String username, String password, String specialization, String[] cities) {
		this.username = username;
		this.password = password;
		this.specialization = specialization;
		this.cities = cities;
	}

	@Override
	public String getIdentity() {
		return "Instructor " + username;
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

	public String[] getCities() {
		return cities;
	}

	public void setCities(String[] cities) {
		this.cities = cities;
	}
}