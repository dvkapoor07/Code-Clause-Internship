package EventManagementSystem.ui;

import EventManagementSystem.database.DatabaseUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScheduleManager{

    public static void showManageSchedulesForm() {
        Stage stage = new Stage();
        stage.setTitle("Manage Schedules");

        // Form components
        Label eventIdLabel = new Label("Event ID:");
        TextField eventIdField = new TextField();
        Label dateTimeLabel = new Label("Date & Time:");
        TextField dateTimeField = new TextField();
        Button addButton = new Button("Add Schedule");

        // Table for displaying schedules
        TableView<Schedule> table = new TableView<>();
        TableColumn<Schedule, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<Schedule, Integer> eventIdColumn = new TableColumn<>("Event ID");
        TableColumn<Schedule, String> dateTimeColumn = new TableColumn<>("Date & Time");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        table.getColumns().addAll(idColumn, eventIdColumn, dateTimeColumn);

        // Load schedules from the database
        ObservableList<Schedule> schedules = loadSchedules();
        table.setItems(schedules);

        addButton.setOnAction(e -> {
            try {
                int eventId = Integer.parseInt(eventIdField.getText());
                String dateTime = dateTimeField.getText();
                if (isValidEventId(eventId)) {
                    addSchedule(eventId, dateTime);
                    schedules.setAll(loadSchedules()); // Refresh the table
                    clearFields(eventIdField, dateTimeField); // Clear fields after adding
                } else {
                    showAlert("Invalid Event ID", "The Event ID does not exist.");
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter a valid Event ID.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Database Error", "An error occurred while adding the schedule.");
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                eventIdLabel, eventIdField,
                dateTimeLabel, dateTimeField,
                addButton,
                new Label("Schedules List"),
                table
        );

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    public static void addSchedule(int eventId, String dateTime) throws SQLException {
        String sql = "INSERT INTO schedules (event_id, date_time) VALUES (?, ?)";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            pstmt.setString(2, dateTime);
            pstmt.executeUpdate();
        }
    }

    public static ObservableList<Schedule> loadSchedules() {
        ObservableList<Schedule> schedules = FXCollections.observableArrayList();
        String sql = "SELECT * FROM schedules";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                schedules.add(new Schedule(rs.getInt("id"), rs.getInt("event_id"), rs.getString("date_time")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    private static boolean isValidEventId(int eventId) {
        String sql = "SELECT COUNT(*) FROM events WHERE id = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void clearFields(TextField eventIdField, TextField dateTimeField) {
        eventIdField.clear();
        dateTimeField.clear();
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Define a Schedule class to match the data in the table
    public static class Schedule {
        private int id;
        private int eventId;
        private String dateTime;

        public Schedule(int id, int eventId, String dateTime) {
            this.id = id;
            this.eventId = eventId;
            this.dateTime = dateTime;
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public int getEventId() { return eventId; }
        public void setEventId(int eventId) { this.eventId = eventId; }

        public String getDateTime() { return dateTime; }
        public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    }
}
