package EventManagementSystem.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class Event {

    private static final String URL = "jdbc:mysql://localhost:3306/em";
    private static final String USER = "root";
    private static final String PASSWORD = "Dvkapoor@07";

    private int id;
    private String title;
    private LocalDate date;
    private String location;

    public Event(int id, String title, LocalDate date, String location) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.location = location;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void addEvent(String title, LocalDate date, String location) {
        String sql = "INSERT INTO events (title, date, location) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setDate(2, Date.valueOf(date));
            pstmt.setString(3, location);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addAttendee(int eventId, String name, String email) {
        String sql = "INSERT INTO attendees (event_id, name, email) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Event> getEvents() {
        ObservableList<Event> events = FXCollections.observableArrayList();
        String sql = "SELECT * FROM events";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                events.add(new Event(rs.getInt("id"), rs.getString("title"), rs.getDate("date").toLocalDate(), rs.getString("location")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
}
