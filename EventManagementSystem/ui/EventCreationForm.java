package EventManagementSystem.ui;

import EventManagementSystem.database.Event; // Update this import
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EventCreationForm {

    public static void showCreateEventForm() {
        Stage stage = new Stage();
        stage.setTitle("Create Event");

        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();
        Label dateLabel = new Label("Date:");
        DatePicker datePicker = new DatePicker();
        Label locationLabel = new Label("Location:");
        TextField locationField = new TextField();
        Button submitButton = new Button("Submit");

        submitButton.setOnAction(e -> {
            String title = titleField.getText();
            LocalDate date = datePicker.getValue();
            String location = locationField.getText();

            // Call method to save event to database
            Event.addEvent(title, date, location); // Update this line
            stage.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, titleField, dateLabel, datePicker, locationLabel, locationField, submitButton);

        Scene scene = new Scene(layout, 300, 250);
        stage.setScene(scene);
        stage.show();
    }
}
