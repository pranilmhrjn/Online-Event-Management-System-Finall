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

public class AttendeeMyEventsPage {

    public static VBox build(Stage stage,
            String username, int userId) {

        EventController ec = new EventController();
        ScrollPane scroll = AdminUtils.scroll();
        VBox content = AdminUtils.content();

        Label pageTitle =
            AdminUtils.pageTitle(
            "My Registrations");

        VBox tableCard = AdminUtils.card();
        Label tableTitle =
            AdminUtils.cardTitle(
            "Events I Have Registered For");

        Label actionMsg =
            AttendeeUtils.msgLabel();

        TableView<ObservableList<String>> table =
            new TableView<>();
        table.setPrefHeight(450);
        table.setPlaceholder(new Label(
            "You have not registered for " +
            "any events yet."));

        String[] cols   = {
            "Event Name", "Date",
            "Location", "Category",
            "Price", "Registered On"};
        int[]    widths = {
            190, 110, 150, 110, 80, 150};
        int[]    idxs   = {1, 2, 3, 5, 6, 10};

        for (int i = 0; i < cols.length; i++) {
            final int idx = idxs[i];
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

        // Status badge column
        TableColumn<ObservableList<String>,
            String> statusCol =
            new TableColumn<>("Status");
        statusCol.setPrefWidth(90);
        statusCol.setCellFactory(col ->
            new TableCell<>() {
            @Override
            protected void updateItem(
                    String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ObservableList<String> row =
                        getTableView().getItems()
                        .get(getIndex());
                    String status =
                        row.size() > 9
                        ? row.get(9) : "Active";
                    setGraphic(
                        AttendeeUtils
                        .statusBadge(status));
                }
            }
        });
        table.getColumns().add(statusCol);

        // Cancel column
        TableColumn<ObservableList<String>, Void>
            cancelCol =
            new TableColumn<>("Action");
        cancelCol.setPrefWidth(130);
        cancelCol.setCellFactory(
            col -> new TableCell<>() {
            Button btn =
                new Button("✘  Cancel");
            {
                btn.setStyle(
                    "-fx-background-color: #e53e3e;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 12px;" +
                    "-fx-background-radius: 5;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 5 12;");
                btn.setOnAction(e -> {
                    ObservableList<String> row =
                        getTableView().getItems()
                        .get(getIndex());
                    int eventId =
                        Integer.parseInt(
                        row.get(0));
                    Alert confirm = new Alert(
                        Alert.AlertType
                        .CONFIRMATION);
                    confirm.setTitle(
                        "Cancel Registration");
                    confirm.setHeaderText(
                        "Cancel: " +
                        row.get(1) + "?");
                    confirm.setContentText(
                        "This will remove " +
                        "your registration.");
                    ButtonType yes =
                        new ButtonType(
                        "Yes, Cancel",
                        ButtonBar.ButtonData
                        .OK_DONE);
                    ButtonType no =
                        new ButtonType(
                        "No, Keep",
                        ButtonBar.ButtonData
                        .CANCEL_CLOSE);
                    confirm.getButtonTypes()
                        .setAll(yes, no);
                    confirm.showAndWait()
                        .ifPresent(r -> {
                        if (r == yes) {
                            ec.cancelRegistration(
                                userId, eventId);
                            AttendeeUtils.setSuccess(
                                actionMsg,
                                "✔ Cancelled: " +
                                row.get(1));
                            loadTable(
                                table, ec, userId);
                        }
                    });
                });
            }
            @Override
            protected void updateItem(
                    Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
        table.getColumns().add(cancelCol);
        loadTable(table, ec, userId);

        tableCard.getChildren().addAll(
            tableTitle, table, actionMsg);
        content.getChildren().addAll(
            pageTitle, tableCard);
        scroll.setContent(content);
        return AdminUtils.wrap(scroll);
    }

    static void loadTable(
            TableView<ObservableList<String>> table,
            EventController ec, int userId) {
        ObservableList<ObservableList<String>>
            data = FXCollections
            .observableArrayList();
        for (String[] row :
            ec.getRegisteredEvents(userId))
            data.add(FXCollections
                .observableArrayList(row));
        table.setItems(data);
    }
}