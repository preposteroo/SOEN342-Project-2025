package com.soen342HB.coursecompass.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import com.soen342HB.coursecompass.offerings.Lesson;
import com.soen342HB.coursecompass.offerings.LessonDAO;
import com.soen342HB.coursecompass.offerings.Offering;
import com.soen342HB.coursecompass.offerings.OfferingDAO;
import com.soen342HB.coursecompass.spaces.City;
import com.soen342HB.coursecompass.spaces.Location;
import com.soen342HB.coursecompass.spaces.Space;

public class Instructor extends PrivateUser {
	private String username;
	private String password; // Phone Number
	private String specialization;
	private String[] cities;

	public void executeCommand(String[] args) {
		switch (args[0]) {
			case "offerings":
				offerings();
				break;
			case "takeoffer":
				takeOffer(args);
				break;
			default:
				super.executeCommand(args);
		}
	}

	public Set<String> getCommands() {
		Set<String> list = super.getCommands();
		list.add("offerings");
		return list;
	}

	public void offerings() {
		System.out.println("Offerings:");
		OfferingDAO offeringDAO = new OfferingDAO();
		Offering[] offerings = offeringDAO.fetchAllFromDb();
		// // Temporary list of cities
		// List<City> citiesList = new ArrayList<City>();
		// for (String city : cities) {
		// citiesList.add(new City(city));
		// }
		// // -------------------------
		// List<Space> spacesList = new ArrayList<Space>();
		// for (City city : citiesList) {
		// for (Location l : city.getLocations()) {
		// for (Space s : l.getSpaces()) {
		// spacesList.add(s);
		// }
		// }
		// }
		for (Offering o : offerings) {
			// if (spacesList.contains(o.getSchedule().getSpace())) {
			System.out.println(o);
			// }
		}
	}

	public void takeOffer(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: takeoffer <offering_id>");
			return;
		}
		OfferingDAO offeringDAO = new OfferingDAO();
		Offering offering = offeringDAO.fetchFromDb(args[1]);
		if (offering != null) {
			System.out.println("Offering taken: " + offering);
		} else {
			System.out.println("Offering not found.");
		}
		Lesson newLesson =
				new Lesson(offering, this, String.format("%h", 0xFFFFFFFF * Math.random()));
		LessonDAO lessonDAO = new LessonDAO();
		lessonDAO.addtoDb(newLesson);
	}

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
