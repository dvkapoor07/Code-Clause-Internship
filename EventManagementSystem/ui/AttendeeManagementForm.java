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

public class AttendeeManagementForm {

    public static void showManageAttendeesForm() {
        Stage stage = new Stage();
        stage.setTitle("Manage Attendees");

        // Form components
        Label eventIdLabel = new Label("Event ID:");
        TextField eventIdField = new TextField();
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Button addButton = new Button("Add Attendee");

        // Table for displaying attendees
        TableView<Attendee> table = new TableView<>();
        TableColumn<Attendee, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<Attendee, Integer> eventIdColumn = new TableColumn<>("Event ID");
        TableColumn<Attendee, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Attendee, String> emailColumn = new TableColumn<>("Email");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        table.getColumns().addAll(idColumn, eventIdColumn, nameColumn, emailColumn);

        // Load attendees from the database
        ObservableList<Attendee> attendees = loadAttendees();
        table.setItems(attendees);

        addButton.setOnAction(e -> {
            try {
                int eventId = Integer.parseInt(eventIdField.getText());
                String name = nameField.getText();
                String email = emailField.getText();
                if (isValidEventId(eventId)) {
                    addAttendee(eventId, name, email);
                    attendees.setAll(loadAttendees()); // Refresh the table
                    clearFields(eventIdField, nameField, emailField); // Clear fields after adding
                } else {
                    showAlert("Invalid Event ID", "The Event ID does not exist.");
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter a valid Event ID.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Database Error", "An error occurred while adding the attendee.");
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                eventIdLabel, eventIdField,
                nameLabel, nameField,
                emailLabel, emailField,
                addButton,
                new Label("Attendees List"),
                table
        );

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    public static void addAttendee(int eventId, String name, String email) throws SQLException {
        String sql = "INSERT INTO attendees (event_id, name, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
        }
    }

    public static ObservableList<Attendee> loadAttendees() {
        ObservableList<Attendee> attendees = FXCollections.observableArrayList();
        String sql = "SELECT * FROM attendees";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                attendees.add(new Attendee(rs.getInt("id"), rs.getInt("event_id"), rs.getString("name"), rs.getString("email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendees;
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

    private static void clearFields(TextField eventIdField, TextField nameField, TextField emailField) {
        eventIdField.clear();
        nameField.clear();
        emailField.clear();
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Define an Attendee class to match the data in the table
    public static class Attendee {
        private int id;
        private int eventId;
        private String name;
        private String email;

        public Attendee(int id, int eventId, String name, String email) {
            this.id = id;
            this.eventId = eventId;
            this.name = name;
            this.email = email;
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public int getEventId() { return eventId; }
        public void setEventId(int eventId) { this.eventId = eventId; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}

