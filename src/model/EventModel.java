package model;

import java.sql.*;
import java.util.*;

public class EventModel {

    public boolean addEvent(String title,String date, String location,String description) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO events (title, date," +
                " location, description) " +
                "VALUES (?, ?, ?, ?)");
            pst.setString(1, title);
            pst.setString(2, date);
            pst.setString(3, location);
            pst.setString(4, description);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(
                "Add error: " + e.getMessage());
            return false;
        }
    }

    public boolean addEvent(String title,
            String date, String location,
            String description, String category,
            String price, int capacity,
            String organizerUsername) {
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement pst =
                conn.prepareStatement(
                "INSERT INTO events (title, date," +
                " location, description, category," +
                " price, capacity," +
                " organizer_username, status)" +
                " VALUES (?,?,?,?,?,?,?,?,'Active')");
            pst.setString(1, title);
            pst.setString(2, date);
            pst.setString(3, location);
            pst.setString(4, description);
            pst.setString(5, category);
            pst.setString(6, price);
            pst.setInt(7, capacity);
            pst.setString(8, organizerUsername);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(
                "Add error: " + e.getMessage());
            return false;
        }
    }

    public boolean addEventWithOrganizer(
            String title, String date, String time,
            String location, String description,
            String category, String price,
            int capacity, int organizerId,
            String organizerUsername) {
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement pst =
                conn.prepareStatement(
                "INSERT INTO events (title, date," +
                " location, description, category," +
                " price, capacity, organizer_id," +
                " organizer_username, status)" +
                " VALUES (?,?,?,?,?,?,?,?,?,'Active')");
            pst.setString(1, title);
            pst.setString(2, date +
                (time.isEmpty() ? "" : " " + time));
            pst.setString(3, location);
            pst.setString(4, description);
            pst.setString(5, category);
            pst.setString(6, price);
            pst.setInt(7, capacity);
            pst.setInt(8, organizerId);
            pst.setString(9, organizerUsername);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(
                "Add error: " + e.getMessage());
            return false;
        }
    }

    public List<String[]> getAllEvents() {
        List<String[]> list = new ArrayList<>();
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            ResultSet rs = conn.prepareStatement(
                "SELECT * FROM events"
            ).executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("title"),
                    rs.getString("date"),
                    rs.getString("location"),
                    rs.getString("description")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String[]> getAllEventsDetailed() {
        List<String[]> list = new ArrayList<>();
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            ResultSet rs = conn.prepareStatement(
                "SELECT id, title, date, " +
                "location, description, " +
                "category, price, capacity, " +
                "registered, status " +
                "FROM events"
            ).executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    safe(rs, "title"),
                    safe(rs, "date"),
                    safe(rs, "location"),
                    safe(rs, "description"),
                    safe(rs, "category"),
                    safe(rs, "price"),
                    String.valueOf(
                        rs.getInt("capacity")),
                    String.valueOf(
                        rs.getInt("registered")),
                    safe(rs, "status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean deleteEvent(int id) {
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement pst =
                conn.prepareStatement(
                "DELETE FROM events WHERE id=?");
            pst.setInt(1, id);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateEvent(int id,
            String title, String date,
            String location, String description) {
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement pst =
                conn.prepareStatement(
                "UPDATE events SET title=?," +
                " date=?, location=?," +
                " description=? WHERE id=?");
            pst.setString(1, title);
            pst.setString(2, date);
            pst.setString(3, location);
            pst.setString(4, description);
            pst.setInt(5, id);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateFullEvent(int id,
            String title, String date,
            String location, String description,
            String category, String price,
            int capacity) {
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement pst =
                conn.prepareStatement(
                "UPDATE events SET title=?," +
                " date=?, location=?," +
                " description=?, category=?," +
                " price=?, capacity=?" +
                " WHERE id=?");
            pst.setString(1, title);
            pst.setString(2, date);
            pst.setString(3, location);
            pst.setString(4, description);
            pst.setString(5, category);
            pst.setString(6, price);
            pst.setInt(7, capacity);
            pst.setInt(8, id);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<String[]> getEventsByOrganizer(
            String username) {
        List<String[]> list = new ArrayList<>();
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement pst =
                conn.prepareStatement(
                "SELECT id, title, date, location," +
                " description, category, price," +
                " capacity, registered, status" +
                " FROM events WHERE" +
                " organizer_username = ?");
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    safe(rs, "title"),
                    safe(rs, "date"),
                    safe(rs, "location"),
                    safe(rs, "description"),
                    safe(rs, "category"),
                    safe(rs, "price"),
                    String.valueOf(
                        rs.getInt("capacity")),
                    String.valueOf(
                        rs.getInt("registered")),
                    safe(rs, "status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String[]> getEventsByOrganizerId(
            int organizerId) {
        List<String[]> list = new ArrayList<>();
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement pst =
                conn.prepareStatement(
                "SELECT id, title, date, location," +
                " description, category, price," +
                " capacity, registered, status" +
                " FROM events WHERE" +
                " organizer_id = ?");
            pst.setInt(1, organizerId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    safe(rs, "title"),
                    safe(rs, "date"),
                    safe(rs, "location"),
                    safe(rs, "description"),
                    safe(rs, "category"),
                    safe(rs, "price"),
                    String.valueOf(
                        rs.getInt("capacity")),
                    String.valueOf(
                        rs.getInt("registered")),
                    safe(rs, "status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String[]> searchOrganizerEvents(
            String username, String keyword) {
        List<String[]> list = new ArrayList<>();
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement pst =
                conn.prepareStatement(
                "SELECT id, title, date, location," +
                " description, category, price," +
                " capacity, registered, status" +
                " FROM events WHERE" +
                " organizer_username=?" +
                " AND title LIKE ?");
            pst.setString(1, username);
            pst.setString(2, "%" + keyword + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    safe(rs, "title"),
                    safe(rs, "date"),
                    safe(rs, "location"),
                    safe(rs, "description"),
                    safe(rs, "category"),
                    safe(rs, "price"),
                    String.valueOf(
                        rs.getInt("capacity")),
                    String.valueOf(
                        rs.getInt("registered")),
                    safe(rs, "status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String[]> filterByCategory(
            String username, String category) {
        if (category.equals("All Categories"))
            return getEventsByOrganizer(username);
        List<String[]> list = new ArrayList<>();
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement pst =
                conn.prepareStatement(
                "SELECT id, title, date, location," +
                " description, category, price," +
                " capacity, registered, status" +
                " FROM events WHERE" +
                " organizer_username=?" +
                " AND category=?");
            pst.setString(1, username);
            pst.setString(2, category);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    safe(rs, "title"),
                    safe(rs, "date"),
                    safe(rs, "location"),
                    safe(rs, "description"),
                    safe(rs, "category"),
                    safe(rs, "price"),
                    String.valueOf(
                        rs.getInt("capacity")),
                    String.valueOf(
                        rs.getInt("registered")),
                    safe(rs, "status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String[]> getAttendeesByEvent(
            int eventId) {
        List<String[]> list = new ArrayList<>();
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement pst =
                conn.prepareStatement(
                "SELECT u.id, u.username," +
                " u.email, r.registered_at" +
                " FROM registrations r" +
                " JOIN users u" +
                " ON r.user_id = u.id" +
                " WHERE r.event_id = ?");
            pst.setInt(1, eventId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt(1)),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean registerForEvent(
            int userId, int eventId) {
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement check =
                conn.prepareStatement(
                "SELECT id FROM registrations" +
                " WHERE user_id=? AND event_id=?");
            check.setInt(1, userId);
            check.setInt(2, eventId);
            ResultSet rs = check.executeQuery();
            if (rs.next()) return false;

            PreparedStatement pst =
                conn.prepareStatement(
                "INSERT INTO registrations" +
                " (user_id, event_id)" +
                " VALUES (?, ?)");
            pst.setInt(1, userId);
            pst.setInt(2, eventId);
            pst.executeUpdate();

            conn.prepareStatement(
                "UPDATE events SET" +
                " registered = registered + 1" +
                " WHERE id = " + eventId)
                .executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(
                "Register error: " +
                e.getMessage());
            return false;
        }
    }

    public boolean cancelRegistration(
            int userId, int eventId) {
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement pst =
                conn.prepareStatement(
                "DELETE FROM registrations" +
                " WHERE user_id=? AND event_id=?");
            pst.setInt(1, userId);
            pst.setInt(2, eventId);
            pst.executeUpdate();

            conn.prepareStatement(
                "UPDATE events SET registered =" +
                " GREATEST(registered - 1, 0)" +
                " WHERE id = " + eventId)
                .executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(
                "Cancel error: " +
                e.getMessage());
            return false;
        }
    }

    public boolean isRegistered(
            int userId, int eventId) {
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement pst =
                conn.prepareStatement(
                "SELECT id FROM registrations" +
                " WHERE user_id=? AND event_id=?");
            pst.setInt(1, userId);
            pst.setInt(2, eventId);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (Exception e) {
            return false;
        }
    }

    public List<String[]> getRegisteredEvents(
            int userId) {
        List<String[]> list = new ArrayList<>();
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement pst =
                conn.prepareStatement(
                "SELECT e.id, e.title, e.date," +
                " e.location, e.description," +
                " e.category, e.price," +
                " e.capacity, e.registered," +
                " e.status, r.registered_at" +
                " FROM events e" +
                " JOIN registrations r" +
                " ON e.id = r.event_id" +
                " WHERE r.user_id = ?");
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt(1)),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7),
                    String.valueOf(rs.getInt(8)),
                    String.valueOf(rs.getInt(9)),
                    rs.getString(10),
                    rs.getString(11)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String[]> searchEvents(
            String keyword, String category,
            String date) {
        List<String[]> list = new ArrayList<>();
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            StringBuilder sql = new StringBuilder(
                "SELECT id, title, date, location," +
                " description, category, price," +
                " capacity, registered, status" +
                " FROM events WHERE 1=1");

            if (keyword != null &&
                !keyword.isEmpty())
                sql.append(
                    " AND title LIKE '%" +
                    keyword + "%'");
            if (category != null &&
                !category.isEmpty() &&
                !category.equals("All Categories"))
                sql.append(
                    " AND category = '" +
                    category + "'");
            if (date != null && !date.isEmpty())
                sql.append(
                    " AND date LIKE '%" +
                    date + "%'");

            ResultSet rs = conn.prepareStatement(
                sql.toString()).executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    safe(rs, "title"),
                    safe(rs, "date"),
                    safe(rs, "location"),
                    safe(rs, "description"),
                    safe(rs, "category"),
                    safe(rs, "price"),
                    String.valueOf(
                        rs.getInt("capacity")),
                    String.valueOf(
                        rs.getInt("registered")),
                    safe(rs, "status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private String safe(
            ResultSet rs, String col) {
        try {
            String v = rs.getString(col);
            return v != null ? v : "";
        } catch (Exception e) {
            return "";
        }
    }
}