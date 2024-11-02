package com.soen342HB.coursecompass.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.soen342HB.coursecompass.offerings.Lesson;
import com.soen342HB.coursecompass.offerings.LessonDAO;
import com.soen342HB.coursecompass.offerings.Offering;
import com.soen342HB.coursecompass.offerings.OfferingDAO;
import com.soen342HB.coursecompass.offerings.Schedule;
import com.soen342HB.coursecompass.offerings.ScheduleDAO;
import com.soen342HB.coursecompass.spaces.City;
import com.soen342HB.coursecompass.spaces.CityDAO;
import com.soen342HB.coursecompass.spaces.Location;
import com.soen342HB.coursecompass.spaces.LocationDAO;
import com.soen342HB.coursecompass.spaces.Space;
import com.soen342HB.coursecompass.spaces.SpaceDAO;
import com.soen342HB.coursecompass.utils.InputManager;
import com.soen342HB.coursecompass.offerings.Booking;

public class Student extends PrivateUser {
    private int id;
    private String username;
    private String password;
    private ArrayList<Lesson> lessons;


    public void executeCommand(String[] args) {
        switch (args[0]) {
            case "viewlessons":
                viewlessons();
                break;
            case "booklesson":
                booklesson(args);
                break;
            case "mylessons":
                mylessons();
                break;
            default:
                super.executeCommand(args);
        }
    }

    public Set<String> getCommands() {
        Set<String> list = super.getCommands();
        list.add("viewlessons");
        list.add("booklesson");
        list.add("mylessons");

        return list;
    }

    public void viewlessons() {
        System.out.println("Which City Would You like to Explore?:");
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
        LessonDAO lessonDAO = new LessonDAO();

        List<Schedule> schedules = space.getSchedules();
        List<Lesson> lessons = lessonDAO.fetchAllFromDb();
        List<Schedule> lessonsForSpace = new ArrayList<>();
        List<Lesson> confirmedLessons = new ArrayList<>();

        for (Lesson lesson : lessons) {
            Schedule lessonSchedule = lesson.getSchedule();
            for (Schedule schedule : schedules) {
                System.out.print(lessonSchedule.getId());
                System.out.print(schedule.getId());
                if (lessonSchedule.getId() == schedule.getId()) {
                    lessonsForSpace.add(schedule);
                    confirmedLessons.add(lesson);
                    break;
                }
            }
        }



        Map<String, List<Schedule>> groupedSchedules =
                scheduleDAO.groupSchedulesByDateRange(lessonsForSpace);
        scheduleDAO.printGroupedSchedules(groupedSchedules, offering, offeringDAO, city, location,
                space, confirmedLessons);


    }

    public void booklesson(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: booklesson <lesson_id>");
            return;
        }
        LessonDAO lessonDAO = new LessonDAO();
        StudentDAO studentDAO = new StudentDAO();
        Student student = studentDAO.fetchFromDb(this.username);
        Lesson lesson = lessonDAO.fetchFromDb(args[1]);
        if (lesson.getAvailable().equals("available")) {
            System.out.println("Lesson booked: " + lesson.getId());
            System.out.println("Is this booking for a dependent? yes or no");
            String dependent = InputManager.getInput();
            if (dependent.equals("yes")) {
                System.out.println("What is your dependent's name?: ");
                String dependent_name = InputManager.getInput();
                System.out.println("What is your dependent's age?: ");
                String dependent_age = InputManager.getInput();

                lessonDAO.addBookingToDb(student.getId(), Integer.parseInt(lesson.getId()),
                        dependent_name, Integer.parseInt(dependent_age));


            }

            lessonDAO.updateAvailability(lesson.getInstructor().getId(),
                    lesson.getSchedule().getId(), "not available");

        } else {
            System.out.println("Lesson not available, it's already been booked!");
        }

    }

    public void mylessons() {
        List<Booking> myBookings;
        LessonDAO lessonDAO = new LessonDAO();
        OfferingDAO offeringDAO = new OfferingDAO();
        Lesson lesson;
        Schedule schedule;
        Offering offering;
        myBookings = lessonDAO.fetchAllBookingsForUserId(this.getId());
        System.out.println("Here are your bookings, " + this.username + "\n");
        for (Booking booking : myBookings) {
            if (booking.getDependentName() != null) {
                System.out.println("This booking is for " + booking.getDependentName() + " who is "
                        + booking.getDependentAge() + " years old.");
            }
            lesson = lessonDAO.fetchFromDb(String.valueOf(booking.getLessonId()));
            schedule = lesson.getSchedule();
            offering = offeringDAO.fetchFromDb(
                    String.valueOf(offeringDAO.getOfferingIdByScheduleId(schedule.getId())));
            System.out.println(offering.getType() + " " + offering.getCourseName()
                    + " lesson with instructor " + lesson.getInstructor().getUsername());
            System.out.println("It takes place from" + schedule.getStartDate() + " to "
                    + schedule.getEndDate() + " on " + schedule.getDayOfWeek() + " between "
                    + schedule.getStartTime() + " and " + schedule.getEndTime());

        }
    }

    public Student(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Student(String username, String password) {
        this.username = username;
        this.password = password;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String getIdentity() {
        return "Student " + username;
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }

}
