package EventManagementSystem.ui;

import EventManagementSystem.database.Event;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate; // Import LocalDate

public class EventListView {

    public static void showEventList() {
        Stage stage = new Stage();
        stage.setTitle("Event List");

        TableView<Event> table = new TableView<>();

        // Create and configure columns
        TableColumn<Event, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<Event, String> titleColumn = new TableColumn<>("Title");
        TableColumn<Event, LocalDate> dateColumn = new TableColumn<>("Date");
        TableColumn<Event, String> locationColumn = new TableColumn<>("Location");

        // Ensure these PropertyValueFactory references match the getters in your Event class
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        // Set column widths (optional)
        idColumn.setPrefWidth(100);
        titleColumn.setPrefWidth(200);
        dateColumn.setPrefWidth(100);
        locationColumn.setPrefWidth(200);

        // Add columns to the table
        table.getColumns().addAll(idColumn, titleColumn, dateColumn, locationColumn);

        // Fetch events from the database and populate the table
        ObservableList<Event> events = Event.getEvents(); // Fetch events
        table.setItems(events);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(new Label("Event List"), table);

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
}
