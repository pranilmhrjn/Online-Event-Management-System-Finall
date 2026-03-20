package view;

import controller.EventController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.util.List;

public class ViewAttendeesPage {

    public static VBox build(
            Stage stage, String username,
            int organizerId) {

        EventController controller = new EventController();
        ScrollPane scroll = AdminUtils.scroll();
        VBox content = AdminUtils.content();

        Label pageTitle =
            AdminUtils.pageTitle("View Attendees");

        // ── Event selector ──
        VBox selectorCard = AdminUtils.card();
        Label selectorTitle = AdminUtils.cardTitle(
            "Select an Event to View Attendees");

        ComboBox<String> eventSelector =
            new ComboBox<>();
        eventSelector.setPromptText(
            "Choose an event...");
        eventSelector.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #cbd5e0;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-font-size: 13px;" +
            "-fx-pref-height: 40px;" +
            "-fx-pref-width: 400px;");

        // Load organizer events into dropdown
        List<String[]> myEvents =
            controller.getEventsByOrganizerId(organizerId);
        ObservableList<String> eventNames =
            FXCollections.observableArrayList();
        java.util.Map<String, Integer> eventMap =
            new java.util.LinkedHashMap<>();

        for (String[] ev : myEvents) {
            String label = ev[1] + " (" + ev[2] + ")";
            eventNames.add(label);
            eventMap.put(label, Integer.parseInt(ev[0]));
        }
        eventSelector.setItems(eventNames);

        Button viewBtn = new Button("View Attendees");
        viewBtn.setStyle(LoginView.primaryBtnStyle());
        viewBtn.setPrefHeight(40);

        HBox selectorRow = new HBox(16,
            eventSelector, viewBtn);
        selectorRow.setAlignment(Pos.CENTER_LEFT);
        selectorCard.getChildren().addAll(
            selectorTitle, selectorRow);

        // ── Attendees Table ──
        VBox tableCard = AdminUtils.card();

        Label tableTitle = new Label("Attendee List");
        tableTitle.setFont(Font.font(
            "Arial", FontWeight.BOLD, 16));
        tableTitle.setTextFill(Color.web("#1b2a4a"));

        Label countLabel = new Label("");
        countLabel.setFont(Font.font("Arial", 13));
        countLabel.setTextFill(Color.web("#4361ee"));

        TableView<ObservableList<String>> table =
            new TableView<>();
        table.setPrefHeight(400);
        table.setPlaceholder(
            new Label(
            "Select an event to view attendees"));

        // Serial number column
        TableColumn<ObservableList<String>,
            String> snCol = new TableColumn<>("#");
        snCol.setPrefWidth(50);
        snCol.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(
                    String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null :
                    String.valueOf(getIndex() + 1));
            }
        });
        table.getColumns().add(snCol);

        String[] cols   =
            {"Username","Email","Registered At"};
        int[]    widths = {180, 260, 160};
        for (int i = 0; i < cols.length; i++) {
            final int idx = i + 1;
            TableColumn<ObservableList<String>,
                String> col =
                new TableColumn<>(cols[i]);
            col.setCellValueFactory(d ->
                new SimpleStringProperty(
                    d.getValue().size() > idx
                    ? d.getValue().get(idx) : ""));
            col.setPrefWidth(widths[i]);
            table.getColumns().add(col);
        }

        Label statusMsg = new Label();
        statusMsg.setFont(Font.font("Arial", 13));

        viewBtn.setOnAction(e -> {
            String selected =
                eventSelector.getValue();
            if (selected == null) {
                statusMsg.setText(
                    "⚠  Please select an event first.");
                statusMsg.setTextFill(
                    Color.web("#e53e3e"));
                return;
            }
            int eventId = eventMap.get(selected);
            List<String[]> attendees =
                controller.getAttendeesByEvent(eventId);

            ObservableList<ObservableList<String>> data =
                FXCollections.observableArrayList();
            for (String[] row : attendees)
                data.add(FXCollections
                    .observableArrayList(row));
            table.setItems(data);

            countLabel.setText(
                "Total Attendees: " +
                attendees.size() + " registered");
            statusMsg.setText("");
        });

        tableCard.getChildren().addAll(
            tableTitle, countLabel, table, statusMsg);
        content.getChildren().addAll(
            pageTitle, selectorCard, tableCard);

        scroll.setContent(content);
        return AdminUtils.wrap(scroll);
    }
}