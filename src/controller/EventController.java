package controller;

import model.EventModel;
import java.util.List;

public class EventController {

    private EventModel eventModel =
        new EventModel();

    public boolean addEvent(String title,
            String date, String location,
            String description) {
        if (title.isEmpty() || date.isEmpty() ||
            location.isEmpty()) return false;
        return eventModel.addEvent(
            title, date, location, description);
    }

    public boolean addEvent(String title,
            String date, String location,
            String description, String category,
            String price, int capacity,
            String organizerUsername) {
        if (title.isEmpty() || date.isEmpty() ||
            location.isEmpty()) return false;
        return eventModel.addEvent(title, date,
            location, description, category,
            price, capacity, organizerUsername);
    }

    public boolean addEventWithOrganizer(
            String title, String date,
            String time, String location,
            String description, String category,
            String price, int capacity,
            int organizerId,
            String organizerUsername) {
        if (title.isEmpty() || date.isEmpty() ||
            location.isEmpty()) return false;
        return eventModel.addEventWithOrganizer(
            title, date, time, location,
            description, category, price,
            capacity, organizerId,
            organizerUsername);
    }

    public List<String[]> getAllEvents() {
        return eventModel.getAllEvents();
    }

    public List<String[]> getAllEventsDetailed() {
        return eventModel.getAllEventsDetailed();
    }

    public boolean deleteEvent(int id) {
        return eventModel.deleteEvent(id);
    }

    public boolean updateEvent(int id,
            String title, String date,
            String location,
            String description) {
        return eventModel.updateEvent(id,
            title, date, location, description);
    }

    public boolean updateFullEvent(int id,
            String title, String date,
            String location, String description,
            String category, String price,
            int capacity) {
        return eventModel.updateFullEvent(id,
            title, date, location, description,
            category, price, capacity);
    }

    public List<String[]> getEventsByOrganizer(
            String username) {
        return eventModel
            .getEventsByOrganizer(username);
    }

    public List<String[]> getEventsByOrganizerId(
            int organizerId) {
        return eventModel
            .getEventsByOrganizerId(organizerId);
    }

    public List<String[]> searchOrganizerEvents(
            String username, String keyword) {
        return eventModel.searchOrganizerEvents(
            username, keyword);
    }

    public List<String[]> filterByCategory(
            String username, String category) {
        return eventModel.filterByCategory(
            username, category);
    }

    public List<String[]> getAttendeesByEvent(
            int eventId) {
        return eventModel
            .getAttendeesByEvent(eventId);
    }

    public boolean registerForEvent(
            int userId, int eventId) {
        return eventModel
            .registerForEvent(userId, eventId);
    }

    public boolean cancelRegistration(
            int userId, int eventId) {
        return eventModel
            .cancelRegistration(userId, eventId);
    }

    public boolean isRegistered(
            int userId, int eventId) {
        return eventModel
            .isRegistered(userId, eventId);
    }

    public List<String[]> getRegisteredEvents(
            int userId) {
        return eventModel
            .getRegisteredEvents(userId);
    }

    public List<String[]> searchEvents(
            String keyword, String category,
            String date) {
        return eventModel.searchEvents(
            keyword, category, date);
    }
}