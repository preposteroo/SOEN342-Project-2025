package com.soen342HB.coursecompass.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.soen342HB.coursecompass.offerings.Booking;
import com.soen342HB.coursecompass.offerings.BookingDAO;
import com.soen342HB.coursecompass.offerings.EDayOfWeek;
import com.soen342HB.coursecompass.offerings.EOfferingMode;
import com.soen342HB.coursecompass.offerings.LessonDAO;
import com.soen342HB.coursecompass.offerings.Offering;
import com.soen342HB.coursecompass.offerings.OfferingDAO;
import com.soen342HB.coursecompass.offerings.Schedule;
import com.soen342HB.coursecompass.offerings.ScheduleDAO;
import com.soen342HB.coursecompass.spaces.City;
import com.soen342HB.coursecompass.spaces.CityDAO;
import com.soen342HB.coursecompass.spaces.LocationDAO;
import com.soen342HB.coursecompass.spaces.Location;
import com.soen342HB.coursecompass.spaces.Space;
import com.soen342HB.coursecompass.spaces.SpaceDAO;
import com.soen342HB.coursecompass.offerings.Lesson;
import com.soen342HB.coursecompass.utils.InputManager;

public class Administrator extends PrivateUser {
    private String username;
    private String password;

    @Override
    public void executeCommand(String[] args) {
        switch (args[0]) {
            case "offerings":
                offerings(args);
                break;
            case "database":
                database();
                break;
            default:
                super.executeCommand(args);
        }
    }

    @Override
    public Set<String> getCommands() {
        Set<String> list = super.getCommands();
        list.add("offerings");
        list.add("database");
        return list;
    }

    private void offerings(String[] flags) {
        // if (flags.length < 2) {
        // System.out.println("Invalid use of the command offerings.");
        // return;
        // }
        // switch (flags[1]) {
        // case "create":
        System.out.println("Creating an offering...");

        System.out.println("Which City is the offering in?:");
        CityDAO cityDAO = new CityDAO();
        City[] cities = cityDAO.fetchAllFromDb();
        for (City city : cities) {
            System.out.println(city.getId() + ". " + city.getCityName().toUpperCase());
        }
        System.out.println("Please type the Number of the City, Or to enter a new City type new:");
        String cityId = InputManager.getInput();
        City city;
        if (cityId.equals("new")) {
            System.out.println("Please enter your new city's name: ");
            String newCityName = InputManager.getInput();
            city = new City(newCityName);
            cityDAO.addtoDb(city);
            city = cityDAO.fetchFromDbByName(city.getCityName());
        } else {
            city = cityDAO.fetchFromDb(cityId);
        }
        System.out.println();

        System.out.println("Here are the locations for " + city.getCityName() + ":");
        city.setLocations(cityDAO.getLocationsForCity(city));
        for (Location location : city.getLocations()) {
            System.out.println(location.getId() + ". " + location.getLocationName().toUpperCase());
        }
        System.out.println("Please type the Number of the Location, Or To Enter a New Location in "
                + city.getCityName() + ", type new: ");

        String locationId = InputManager.getInput();
        LocationDAO locationDAO = new LocationDAO();
        Location location;
        if (locationId.equals("new")) {
            System.out.println("Please enter your new location's name: ");
            String newLocationName = InputManager.getInput();
            location = new Location(newLocationName);
            locationDAO.addtoDb(location);
            location.setId(locationDAO.getLocationIdByName(location.getLocationName()));
            cityDAO.locationToCity(location, city);
        } else {
            location = locationDAO.fetchFromDb(locationId);
        }
        System.out.println();

        System.out.println("Here are the spaces for " + location.getLocationName() + ":");

        location.setSpaces(locationDAO.getSpacesForLocation(location));
        for (Space space : location.getSpaces()) {
            System.out.println(space.getId() + ". " + space.getSpaceName().toUpperCase());
        }

        System.out.println("Please type the Number of the Space, Or To Enter a New Space in "
                + location.getLocationName() + ", type new: ");
        String spaceId = InputManager.getInput();
        SpaceDAO spaceDAO = new SpaceDAO();
        Space space;

        if (spaceId.equals("new")) {
            System.out.println("Please enter your new space's name: ");
            String newSpaceName = InputManager.getInput();
            space = new Space(newSpaceName);
            spaceDAO.addtoDb(space);
            space.setId(spaceDAO.getSpaceIdByName(space.getSpaceName()));
            locationDAO.spaceToLocation(space, location);
        } else {
            space = spaceDAO.fetchFromDb(spaceId);
        }


        System.out.println("Create a New Offering for " + space.getSpaceName());
        System.out.println("The course mode can be 'group' or 'private'.");
        System.out.print("Enter the course mode: ");
        EOfferingMode courseType = EOfferingMode.from(InputManager.getInput());
        System.out.println("What type of course is it? (Judo,Yoga...etc)");
        String courseName = InputManager.getInput();
        System.out.print("Enter the start date: ");
        String startDate = InputManager.getInput();
        System.out.print("Enter the end date: ");
        String endDate = InputManager.getInput();
        System.out.print("Enter the day of the week: ");
        String dayOfWeek = InputManager.getInput();
        System.out.print("Enter the start time: ");
        String startTime = InputManager.getInput();
        System.out.print("Enter the end time: ");
        String endTime = InputManager.getInput();

        Schedule schedule = new Schedule(startDate, endDate, EDayOfWeek.from(dayOfWeek), startTime,
                endTime, space);
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        scheduleDAO.addtoDb(schedule);
        scheduleDAO.scheduleToSpace(space, schedule);

        Offering offering = new Offering(courseType, courseName); // and this
        offering.setSchedule(schedule); // changed this
        OfferingDAO offeringDAO = new OfferingDAO();
        offeringDAO.addtoDb(offering);
        offeringDAO.offeringToSchedule(offering, schedule);
        // break;
        // default:
        // System.out.println("Invalid use of the command offerings.");
        // }
    }

    public void database() {
        System.out.println("What would you like to view in the database?");
        System.out.println("1. Offerings" + "\n" + "2. Lessons" + "\n" + "3. Bookings" + "\n"
                + "4. All User Accounts" + "\n" + "5. Cities" + "\n" + "6. Locations" + "\n"
                + "Spaces");
        String answer = InputManager.getInput();
        if (answer.equals("1")) {
            System.out.println("Here are all of the offerings in the system: ");
            OfferingDAO offeringDAO = new OfferingDAO();
            offeringDAO.fetchAllFromDb();
        } else if (answer.equals("2")) {
            System.out.println("Here are all the lessons in the system:");
            LessonDAO lessonDAO = new LessonDAO();
            List<Lesson> lessons = lessonDAO.fetchAllFromDb();
            lessonDAO.printLessons(lessons);

        } else if (answer.equals("3")) {
            BookingDAO bookingDAO = new BookingDAO();
            LessonDAO lessonDAO = new LessonDAO();
            OfferingDAO offeringDAO = new OfferingDAO();
            Lesson lesson;
            Schedule schedule;
            Offering offering;
            System.out.println("Here are all the bookings in the system:");
            List<Booking> bookings = bookingDAO.fetchAllFromDb();
            for (Booking booking : bookings) {
                if (booking.getDependentName() != null) {
                    System.out.println("Booking ID: " + booking.getId() + ". This booking is for "
                            + booking.getDependentName() + " who is " + booking.getDependentAge()
                            + " years old.");
                }
                lesson = lessonDAO.fetchFromDb(String.valueOf(booking.getLessonId()));
                schedule = lesson.getSchedule();
                offering = offeringDAO.fetchFromDb(
                        String.valueOf(offeringDAO.getOfferingIdByScheduleId(schedule.getId())));
                System.out.println(offering.getType() + " " + offering.getCourseName()
                        + " lesson with instructor " + lesson.getInstructor().getUsername());
                System.out.println("It takes place from " + schedule.getStartDate() + " to "
                        + schedule.getEndDate() + " on " + schedule.getDayOfWeek() + " between "
                        + schedule.getStartTime() + " and " + schedule.getEndTime());

            }

        } else {
            System.out.println("Sorry, that wasn't an option.");
        }


    }



    public Administrator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getIdentity() {
        return "Administrator " + username;
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
}
