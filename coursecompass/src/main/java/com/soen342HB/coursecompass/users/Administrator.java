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
            case "makeoffering":
                makeoffering();
                break;
            case "database":
                database();
                break;
            case "makeuser":
                makeuser();
                break;
            case "viewofferings":
                viewofferings();
                break;
            case "updatedb":
                updateDb();
                break;
            case "removefromdb":
                removeFromDb();
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
        list.add("makeuser");
        list.add("viewofferings");
        list.add("updatedb");
        list.add("removefromdb");
        return list;
    }

    private void makeoffering() {
        System.out.println("Creating an offering...");

        System.out.println("Which City is the offering in?:");
        CityDAO cityDAO = new CityDAO();
        City[] cities = cityDAO.fetchAllFromDb();
        for (City city : cities) {
            System.out.println(city.getId() + ". " + city.getCityName().toUpperCase());
        }
        System.out.println("Please type the name of the City, Or to enter a new City type new:");
        String cityId = InputManager.getInput();
        City city = null;
        if (cityId.equals("new")) {
            System.out.println("Please enter your new city's name: ");
            String newCityName = InputManager.getInput();
            city = new City(newCityName);
            cityDAO.addtoDb(city);
            city = cityDAO.fetchFromDbByName(city.getCityName());
        } else {
            city = cityDAO.fetchFromDbByName(cityId);
        }

        if (city == null) {
            return;
        }
        System.out.println();

        System.out.println("Here are the locations for " + city.getCityName() + ":");
        city.setLocations(cityDAO.getLocationsForCity(city));
        for (Location location : city.getLocations()) {
            System.out.println(location.getId() + ". " + location.getLocationName().toUpperCase());
        }
        System.out.println("Please type the name of the Location, Or To Enter a New Location in "
                + city.getCityName() + ", type new: ");

        String locationId = InputManager.getInput();
        LocationDAO locationDAO = new LocationDAO();
        Location location = null;
        if (locationId.equals("new")) {
            System.out.println("Please enter your new location's name: ");
            String newLocationName = InputManager.getInput();
            location = new Location(newLocationName);
            locationDAO.addtoDb(location);
            location.setId(locationDAO.getLocationIdByName(location.getLocationName()));
            cityDAO.locationToCity(location, city);
        } else {
            location = locationDAO.fetchFromDbByName(locationId);
        }

        if (location == null) {
            return;
        }
        System.out.println();

        System.out.println("Here are the spaces for " + location.getLocationName() + ":");

        location.setSpaces(locationDAO.getSpacesForLocation(location));
        for (Space space : location.getSpaces()) {
            System.out.println(space.getId() + ". " + space.getSpaceName().toUpperCase());
        }

        System.out.println("Please type the name of the Space, Or To Enter a New Space in "
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
            space = spaceDAO.fetchFromDbByName(spaceId);
        }


        System.out.println("Create a New Offering for " + space.getSpaceName());
        System.out.println("The course mode can be 'group' or 'private'.");
        System.out.print("Enter the course mode: ");
        EOfferingMode courseType = EOfferingMode.from(InputManager.getInput());
        System.out.println("What type of course is it? (Judo,Yoga...etc)");
        String courseName = InputManager.getInput();
        System.out.print("Enter the start date (yyyy-mm-dd): ");
        String startDate = InputManager.getInput();
        System.out.print("Enter the end date (yyyy-mm-dd): ");
        String endDate = InputManager.getInput();
        System.out.print("Enter the day of the week: ");
        String dayOfWeek = InputManager.getInput();
        System.out.print("Enter the start time (24-hr HH:MM): ");
        String startTime = InputManager.getInput();
        System.out.print("Enter the end time (24-hr HH:MM): ");
        String endTime = InputManager.getInput();

        Offering offering = new Offering(courseType, courseName); // and this
        OfferingDAO offeringDAO = new OfferingDAO();
        offeringDAO.addtoDb(offering);

        Schedule schedule = new Schedule(startDate, endDate, EDayOfWeek.from(dayOfWeek), startTime,
                endTime, space);
        schedule.setOfferingId(offering.getId());
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        scheduleDAO.addtoDb(schedule);
        scheduleDAO.scheduleToSpace(space, schedule);
        // break;
        // default:
        // System.out.println("Invalid use of the command offerings.");
        // }
    }

    public void database() {
        System.out.println("What would you like to view in the database? (enter number)");
        System.out.println("1. Offerings" + "\n" + "2. Lessons" + "\n" + "3. Bookings" + "\n"
                + "4. All User Accounts" + "\n" + "5. Cities" + "\n" + "6. Locations" + "\n"
                + "7. Spaces");
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
        } else if (answer.equals("4")) {
            System.out.println("Here are all the user accounts in the system: ");
            AdministratorDAO adminDAO = new AdministratorDAO();
            InstructorDAO instructorDAO = new InstructorDAO();
            StudentDAO studentDAO = new StudentDAO();
            List<Administrator> admins = adminDAO.fetchAllFromDb();
            List<Instructor> instructors = instructorDAO.fetchAllFromDb();
            List<Student> students = studentDAO.fetchAllFromDb();
            System.out.println("Administrators: ");
            for (Administrator admin : admins) {
                System.out.println(admin.getUsername());
            }
            System.out.println("Instructors: ");
            for (Instructor instructor : instructors) {
                System.out.println(instructor.getUsername());
            }
            System.out.println("Students: ");
            for (Student student : students) {
                System.out.println(student.getUsername());
            }
        } else if (answer.equals("5")) {
            System.out.println("Here are all the cities in the system: ");
            CityDAO cityDAO = new CityDAO();
            City[] cities = cityDAO.fetchAllFromDb();
            for (City city : cities) {
                System.out.println(city.getCityName());
            }
        } else if (answer.equals("6")) {
            System.out.println("Here are all the locations in the system: ");
            LocationDAO locationDAO = new LocationDAO();
            List<Location> locations = locationDAO.fetchAllFromDb();
            for (Location location : locations) {
                System.out.println(location.getLocationName());
            }
        } else if (answer.equals("7")) {
            System.out.println("Here are all the spaces in the system: ");
            SpaceDAO spaceDAO = new SpaceDAO();
            List<Space> spaces = spaceDAO.fetchAllFromDb();
            for (Space space : spaces) {
                System.out.println(space.getSpaceName());
            }

        } else {
            System.out.println("Sorry, that wasn't an option.");
        }


    }

    public void updateDb() {
        System.out.println("What would you like to update in the database? (enter number)");
        System.out.println("1. Offerings" + "\n" + "2. Lessons" + "\n" + "3. Bookings" + "\n"
                + "4. All User Accounts" + "\n" + "5. Cities" + "\n" + "6. Locations" + "\n"
                + "7. Spaces");
        String answer = InputManager.getInput();
        if (answer.equals("1")) {
            System.out
                    .println("Which offering would you like to update? (enter id or -1 to cancel)");
            String offeringId = InputManager.getInput();
            if (offeringId.equals("-1")) {
                return;
            }
            OfferingDAO offeringDAO = new OfferingDAO();
            Offering offering = offeringDAO.fetchFromDb(offeringId);
            if (offering == null) {
                System.out.println("Offering not found.");
                return;
            }
            System.out.println("What would you like to update?");
            System.out.println("1. Course Name" + "\n" + "2. Course Mode" + "\n" + "3. Schedule");
            String updateChoice = InputManager.getInput();
            switch (updateChoice) {
                case "1":
                    System.out.print("Enter the new course name: ");
                    String newCourseName = InputManager.getInput();
                    offering.setCourseName(newCourseName);
                    offeringDAO.updateDb(offering);
                    break;
                case "2":
                    System.out.print("Enter the new course mode: ");
                    EOfferingMode newCourseMode = EOfferingMode.from(InputManager.getInput());
                    offering.setType(newCourseMode);
                    offeringDAO.updateDb(offering);
                    break;
                case "3":
                    System.out.println("Here is the current schedule for this offering:");
                    Schedule schedule = offering.getSchedule();
                    System.out.println("Start Date: " + schedule.getStartDate());
                    System.out.println("End Date: " + schedule.getEndDate());
                    System.out.println("Day of the Week: " + schedule.getDayOfWeek());
                    System.out.println("Start Time: " + schedule.getStartTime());
                    System.out.println("End Time: " + schedule.getEndTime());
                    System.out.println("Would you like to update this schedule? (y/n)");
                    String updateSchedule = InputManager.getInput();
                    if (updateSchedule.equals("y")) {
                        System.out.print("Enter the new start date (yyyy-mm-dd): ");
                        String newStartDate = InputManager.getInput();
                        System.out.print("Enter the new end date (yyyy-mm-dd): ");
                        String newEndDate = InputManager.getInput();
                        System.out.print("Enter the new day of the week: ");
                        String newDayOfWeek = InputManager.getInput();
                        System.out.print("Enter the new start time (24-hr HH:MM): ");
                        String newStartTime = InputManager.getInput();
                        System.out.print("Enter the new end time (24-hr HH:MM): ");
                        String newEndTime = InputManager.getInput();
                        schedule.setStartDate(newStartDate);
                        schedule.setEndDate(newEndDate);
                        schedule.setDayOfWeek(EDayOfWeek.from(newDayOfWeek));
                        schedule.setStartTime(newStartTime);
                        schedule.setEndTime(newEndTime);
                        ScheduleDAO scheduleDAO = new ScheduleDAO();
                        scheduleDAO.updateDb(schedule);
                    }
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } else if (answer.equals("2")) {
            System.out.println("Which lesson would you like to update? (enter id or -1 to cancel)");
            String lessonId = InputManager.getInput();
            if (lessonId.equals("-1")) {
                return;
            }
            LessonDAO lessonDAO = new LessonDAO();
            Lesson lesson = lessonDAO.fetchFromDb(lessonId);
            if (lesson == null) {
                System.out.println("Lesson not found.");
                return;
            }
            System.out.println("What would you like to update?");
            System.out.println("1. Instructor" + "\n" + "2. Schedule");
            String updateChoice = InputManager.getInput();
            switch (updateChoice) {
                case "1":
                    System.out.print("Enter the new instructor's username: ");
                    String newInstructorUsername = InputManager.getInput();
                    InstructorDAO instructorDAO = new InstructorDAO();
                    Instructor newInstructor = instructorDAO.fetchFromDb(newInstructorUsername);
                    if (newInstructor == null) {
                        System.out.println("Instructor not found.");
                        return;
                    }
                    lesson.setInstructor(newInstructor);
                    lessonDAO.updateDb(lesson);
                    break;
                case "2":
                    System.out.println("Here is the current schedule for this lesson:");
                    Schedule schedule = lesson.getSchedule();
                    System.out.println("Start Date: " + schedule.getStartDate());
                    System.out.println("End Date: " + schedule.getEndDate());
                    System.out.println("Day of the Week: " + schedule.getDayOfWeek());
                    System.out.println("Start Time: " + schedule.getStartTime());
                    System.out.println("End Time: " + schedule.getEndTime());
                    System.out.println("Would you like to update this schedule? (y/n)");
                    String updateSchedule = InputManager.getInput();
                    if (updateSchedule.equals("y")) {
                        System.out.print("Enter the new start date (yyyy-mm-dd): ");
                        String newStartDate = InputManager.getInput();
                        System.out.print("Enter the new end date (yyyy-mm-dd): ");
                        String newEndDate = InputManager.getInput();
                        System.out.print("Enter the new day of the week: ");
                        String newDayOfWeek = InputManager.getInput();
                        System.out.print("Enter the new start time (24-hr HH:MM): ");
                        String newStartTime = InputManager.getInput();
                        System.out.print("Enter the new end time (24-hr HH:MM): ");
                        String newEndTime = InputManager.getInput();
                        schedule.setStartDate(newStartDate);
                        schedule.setEndDate(newEndDate);
                        schedule.setDayOfWeek(EDayOfWeek.from(newDayOfWeek));
                        schedule.setStartTime(newStartTime);
                        schedule.setEndTime(newEndTime);
                        ScheduleDAO scheduleDAO = new ScheduleDAO();
                        scheduleDAO.updateDb(schedule);
                    }
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } else if (answer.equals("3")) {
            System.out
                    .println("Which booking would you like to update? (enter id or -1 to cancel)");
            String bookingId = InputManager.getInput();
            if (bookingId.equals("-1")) {
                return;
            }
            BookingDAO bookingDAO = new BookingDAO();
            Booking booking = bookingDAO.fetchFromDb(bookingId);
            if (booking == null) {
                System.out.println("Booking not found.");
                return;
            }
            System.out.println("What would you like to update?");
            System.out.println("1. Dependent Name" + "\n" + "2. Dependent Age");
            String updateChoice = InputManager.getInput();
            switch (updateChoice) {
                case "1":
                    System.out.print("Enter the new dependent name: ");
                    String newDependentName = InputManager.getInput();
                    if (newDependentName.equals("")) {
                        booking.setDependentName(null);
                        booking.setDependentAge(-1);
                    }
                    booking.setDependentName(newDependentName);
                    bookingDAO.updateDb(booking);
                    break;
                case "2":
                    System.out.print("Enter the new dependent age: ");
                    int newDependentAge = Integer.parseInt(InputManager.getInput());
                    booking.setDependentAge(newDependentAge);
                    bookingDAO.updateDb(booking);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } else if (answer.equals("4")) {
            System.out.println(
                    "Which user account would you like to update? (enter username or -1 to cancel)");
            String username = InputManager.getInput();
            if (username.equals("-1")) {
                return;
            }
            System.out.println("What would you like to update?");
            System.out.println("1. Password");
            String updateChoice = InputManager.getInput();
            switch (updateChoice) {
                case "1":
                    System.out.print("Enter the new password: ");
                    String newPassword = InputManager.getInput();
                    AdministratorDAO adminDAO = new AdministratorDAO();
                    InstructorDAO instructorDAO = new InstructorDAO();
                    StudentDAO studentDAO = new StudentDAO();
                    Administrator admin = adminDAO.fetchFromDb(username);
                    Instructor instructor = instructorDAO.fetchFromDb(username);
                    Student student = studentDAO.fetchFromDb(username);
                    if (admin != null) {
                        admin.setPassword(newPassword);
                        adminDAO.updateDb(admin);
                    } else if (instructor != null) {
                        instructor.setPassword(newPassword);
                        instructorDAO.updateDb(instructor);
                    } else if (student != null) {
                        student.setPassword(newPassword);
                        studentDAO.updateDb(student);
                    } else {
                        System.out.println("User not found.");
                    }
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } else if (answer.equals("5")) {
            System.out.println(
                    "Which city would you like to update? (enter city name or -1 to cancel)");
            String cityName = InputManager.getInput();
            if (cityName.equals("-1")) {
                return;
            }
            CityDAO cityDAO = new CityDAO();
            City city = cityDAO.fetchFromDbByName(cityName);
            if (city == null) {
                System.out.println("City not found.");
                return;
            }
            System.out.println("What would you like to update?");
            System.out.println("1. City Name");
            String updateChoice = InputManager.getInput();
            switch (updateChoice) {
                case "1":
                    System.out.print("Enter the new city name: ");
                    String newCityName = InputManager.getInput();
                    city.setCityName(newCityName);
                    cityDAO.updateDb(city);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } else if (answer.equals("6")) {
            System.out.println(
                    "Which location would you like to update? (enter location name or -1 to cancel)");
            String locationName = InputManager.getInput();
            if (locationName.equals("-1")) {
                return;
            }
            LocationDAO locationDAO = new LocationDAO();
            Location location = locationDAO.fetchFromDbByName(locationName);
            if (location == null) {
                System.out.println("Location not found.");
                return;
            }
            System.out.println("What would you like to update?");
            System.out.println("1. Location Name");
            String updateChoice = InputManager.getInput();
            switch (updateChoice) {
                case "1":
                    System.out.print("Enter the new location name: ");
                    String newLocationName = InputManager.getInput();
                    location.setLocationName(newLocationName);
                    locationDAO.updateDb(location);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } else if (answer.equals("7")) {
            System.out.println(
                    "Which space would you like to update? (enter space name or -1 to cancel)");
            String spaceName = InputManager.getInput();
            if (spaceName.equals("-1")) {
                return;
            }
            SpaceDAO spaceDAO = new SpaceDAO();
            Space space = spaceDAO.fetchFromDbByName(spaceName);
            if (space == null) {
                System.out.println("Space not found.");
                return;
            }
            System.out.println("What would you like to update?");
            System.out.println("1. Space Name");
            String updateChoice = InputManager.getInput();
            switch (updateChoice) {
                case "1":
                    System.out.print("Enter the new space name: ");
                    String newSpaceName = InputManager.getInput();
                    space.setSpaceName(newSpaceName);
                    spaceDAO.updateDb(space);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } else {
            System.out.println("Sorry, that wasn't an option.");
        }
    }

    public void removeFromDb() {
        System.out.println("What would you like to remove from the database? (enter number)");
        System.out.println("1. Offerings" + "\n" + "2. Lessons" + "\n" + "3. Bookings" + "\n"
                + "4. All User Accounts" + "\n" + "5. Cities" + "\n" + "6. Locations" + "\n"
                + "7. Spaces");
        String answer = InputManager.getInput();
        if (answer.equals("1")) {
            System.out
                    .println("Which offering would you like to remove? (enter id or -1 to cancel)");
            String offeringId = InputManager.getInput();
            if (offeringId.equals("-1")) {
                return;
            }
            OfferingDAO offeringDAO = new OfferingDAO();
            Offering offering = offeringDAO.fetchFromDb(offeringId);
            if (offering == null) {
                System.out.println("Offering not found.");
                return;
            }
            offeringDAO.removeFromDb(offering);
        } else if (answer.equals("2")) {
            System.out.println("Which lesson would you like to remove? (enter id or -1 to cancel)");
            String lessonId = InputManager.getInput();
            if (lessonId.equals("-1")) {
                return;
            }
            LessonDAO lessonDAO = new LessonDAO();
            Lesson lesson = lessonDAO.fetchFromDb(lessonId);
            if (lesson == null) {
                System.out.println("Lesson not found.");
                return;
            }
            lessonDAO.removeFromDb(lesson);
        } else if (answer.equals("3")) {
            System.out
                    .println("Which booking would you like to remove? (enter id or -1 to cancel)");
            String bookingId = InputManager.getInput();
            if (bookingId.equals("-1")) {
                return;
            }
            BookingDAO bookingDAO = new BookingDAO();
            Booking booking = bookingDAO.fetchFromDb(bookingId);
            if (booking == null) {
                System.out.println("Booking not found.");
                return;
            }
            bookingDAO.removeFromDb(booking);
        } else if (answer.equals("4")) {
            System.out.println(
                    "Which user account would you like to remove? (enter username or -1 to cancel)");
            String username = InputManager.getInput();
            if (username.equals("-1")) {
                return;
            }
            AdministratorDAO adminDAO = new AdministratorDAO();
            InstructorDAO instructorDAO = new InstructorDAO();
            StudentDAO studentDAO = new StudentDAO();
            Administrator admin = adminDAO.fetchFromDb(username);
            Instructor instructor = instructorDAO.fetchFromDb(username);
            Student student = studentDAO.fetchFromDb(username);
            if (admin != null) {
                adminDAO.removeFromDb(admin);
            } else if (instructor != null) {
                instructorDAO.removeFromDb(instructor);
            } else if (student != null) {
                studentDAO.removeFromDb(student);
            } else {
                System.out.println("User not found.");
            }
        } else if (answer.equals("5")) {
            System.out.println(
                    "Which city would you like to remove? (enter city name or -1 to cancel)");
            String cityName = InputManager.getInput();
            if (cityName.equals("-1")) {
                return;
            }
            CityDAO cityDAO = new CityDAO();
            City city = cityDAO.fetchFromDbByName(cityName);
            if (city == null) {
                System.out.println("City not found.");
                return;
            }
            cityDAO.removeFromDb(city);
        } else if (answer.equals("6")) {
            System.out.println(
                    "Which location would you like to remove? (enter location name or -1 to cancel)");
            String locationName = InputManager.getInput();
            if (locationName.equals("-1")) {
                return;
            }
            LocationDAO locationDAO = new LocationDAO();
            Location location = locationDAO.fetchFromDbByName(locationName);
            if (location == null) {
                System.out.println("Location not found.");
                return;
            }
            locationDAO.removeFromDb(location);
        } else if (answer.equals("7")) {
            System.out.println(
                    "Which space would you like to remove? (enter space name or -1 to cancel)");
            String spaceName = InputManager.getInput();
            if (spaceName.equals("-1")) {
                return;
            }
            SpaceDAO spaceDAO = new SpaceDAO();
            Space space = spaceDAO.fetchFromDbByName(spaceName);
            if (space == null) {
                System.out.println("Space not found.");
                return;
            }
            spaceDAO.removeFromDb(space);
        } else {
            System.out.println("Sorry, that wasn't an option.");
        }
    }

    private void makeuser() {
        System.out.println("Are you an administrator, an instructor or a student?");
        System.out.print("Type 'admin', 'instructor' or 'student': ");
        String userType = InputManager.getInput();
        System.out.print("Username: ");
        String username = InputManager.getInput();
        System.out.print("Password: ");
        String password = InputManager.getInput();
        System.out.print("Confirm password: ");
        String confirmPassword = InputManager.getInput();
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match. Operation failed.");
            return;
        }
        switch (userType) {
            case "admin":
                Administrator admin = new Administrator(username, password);
                AdministratorDAO adminDAO = new AdministratorDAO();
                adminDAO.addtoDb(admin);
                break;
            case "instructor":
                System.out.print("Specialization: ");
                String specialization = InputManager.getInput();
                System.out.print("Cities (comma-separated): ");
                String cities = InputManager.getInput().toLowerCase();
                Instructor instructor =
                        new Instructor(username, password, specialization, cities.split(",\\s*"));
                InstructorDAO instructorDAO = new InstructorDAO();
                instructorDAO.addtoDb(instructor);
                break;
            case "student":
                Student student = new Student(username, password);
                StudentDAO studentDAO = new StudentDAO();
                studentDAO.addtoDb(student);
                break;
            default:
                System.out.println("Invalid user type");
                return;
        }
    }

    public void viewofferings() {
        System.out.println("Here are all the offerings in the system: ");
        OfferingDAO offeringDAO = new OfferingDAO();
        offeringDAO.fetchAllFromDb();
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
