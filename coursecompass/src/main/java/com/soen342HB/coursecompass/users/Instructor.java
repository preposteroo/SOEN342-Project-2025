package com.soen342HB.coursecompass.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.soen342HB.coursecompass.offerings.Lesson;
import com.soen342HB.coursecompass.offerings.LessonDAO;
import com.soen342HB.coursecompass.offerings.Offering;
import com.soen342HB.coursecompass.offerings.OfferingDAO;
import com.soen342HB.coursecompass.spaces.City;
import com.soen342HB.coursecompass.spaces.CityDAO;
import com.soen342HB.coursecompass.spaces.Location;
import com.soen342HB.coursecompass.spaces.LocationDAO;
import com.soen342HB.coursecompass.spaces.Space;
import com.soen342HB.coursecompass.spaces.SpaceDAO;
import com.soen342HB.coursecompass.utils.InputManager;
import com.soen342HB.coursecompass.offerings.Schedule;
import com.soen342HB.coursecompass.offerings.ScheduleDAO;


public class Instructor extends PrivateUser {
	private int id;
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
		list.add("takeoffer");
		return list;
	}

	public void offerings() {
		System.out.println("Which City Are You Looking to Teach in?:");
		CityDAO cityDAO = new CityDAO();
		City[] cities = cityDAO.fetchAllFromDb();
		for (City city : cities) {
			System.out.println(city.getId() + ". " + city.getCityName().toUpperCase());
		}
		System.out.println("Please type the Number of the City:");
		String cityId = InputManager.getInput();
		City city;
		city = cityDAO.fetchFromDb(cityId);
		System.out.println("Here are the locations for " + city.getCityName());
		city.setLocations(cityDAO.getLocationsForCity(city));
		for (Location location : city.getLocations()) {
			System.out.println(location.getId() + ". " + location.getLocationName().toUpperCase());
		}
		System.out.println("Please type the Number of the Location");

		String locationId = InputManager.getInput();
		LocationDAO locationDAO = new LocationDAO();
		Location location;
		location = locationDAO.fetchFromDb(locationId);

		System.out.println("Here are the spaces for " + location.getLocationName());

		location.setSpaces(locationDAO.getSpacesForLocation(location));
		for (Space space : location.getSpaces()) {
			System.out.println(space.getId() + ". " + space.getSpaceName().toUpperCase());
		}

		System.out.println("Please type the Number of the Space");
		String spaceId = InputManager.getInput();
		SpaceDAO spaceDAO = new SpaceDAO();
		Space space;
		space = spaceDAO.fetchFromDb(spaceId);
		OfferingDAO offeringDAO = new OfferingDAO();
		Offering offering = new Offering();
		space.setSchedules(spaceDAO.getSchedulesForSpace(space));
		ScheduleDAO scheduleDAO = new ScheduleDAO();
		List<Lesson> lessons = new ArrayList<>();;

		List<Schedule> schedules = space.getSchedules();
		Map<String, List<Schedule>> groupedSchedules =
				scheduleDAO.groupSchedulesByDateRange(schedules);
		scheduleDAO.printGroupedSchedules(groupedSchedules, offering, offeringDAO, city, location,
				space, lessons);

	}

	public void takeOffer(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: takeoffer <offering_id>");
			return;
		}
		ScheduleDAO scheduleDAO = new ScheduleDAO();
		Schedule schedule = scheduleDAO.fetchFromDb(args[1]);
		if (schedule != null) {
			scheduleDAO.scheduleToInstructor(schedule, this);
			System.out.println("Offering taken: " + schedule.getId());
		} else {
			System.out.println("Offering not found.");
		}

	}

	public Instructor() {};

	public Instructor(String username, String password, String specialization, String[] cities) {
		this.username = username;
		this.password = password;
		this.specialization = specialization;
		this.cities = cities;
	}

	public Instructor(int id, String username, String password, String specialization,
			String[] cities) {
		this.id = id;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
