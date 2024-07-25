package EventManagementSystem;

import EventManagementSystem.ui.AttendeeManagementForm;
import EventManagementSystem.ui.EventCreationForm;
import EventManagementSystem.ui.EventListView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Event Management System");

        // Main Menu
        Button createEventButton = new Button("Create Event");
        Button viewEventsButton = new Button("View Events");
        Button manageAttendeesButton = new Button("Manage Attendees");

        // Set button actions
        createEventButton.setOnAction(e -> EventCreationForm.showCreateEventForm());
        viewEventsButton.setOnAction(e -> EventListView.showEventList());
        manageAttendeesButton.setOnAction(e -> AttendeeManagementForm.showManageAttendeesForm());

        VBox root = new VBox(10);
        root.getChildren().addAll(createEventButton, viewEventsButton, manageAttendeesButton);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
