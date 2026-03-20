package view;

import controller.EventController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.util.List;

public class OrganizerEventsPage {

    public static VBox build(
            Stage stage, String username, int organizerId) {

        EventController controller = new EventController();
        ScrollPane scroll = AdminUtils.scroll();
        VBox content = AdminUtils.content();

        // ── Header ──
        HBox headerRow = new HBox();
        headerRow.setAlignment(Pos.CENTER_LEFT);
        Label pageTitle = AdminUtils.pageTitle("My Events");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button createBtn = new Button("+ Create Event");
        createBtn.setStyle(LoginView.primaryBtnStyle());
        createBtn.setPrefHeight(40);
        createBtn.setOnAction(e ->
            OrganizerDashboardView.show(stage, username));
        headerRow.getChildren().addAll(
            pageTitle, spacer, createBtn);

        // ── Search + Filter ──
        VBox searchCard = AdminUtils.card();
        TextField searchField = new TextField();
        searchField.setPromptText("Search events...");
        searchField.setStyle(LoginView.inputStyle());
        searchField.setPrefWidth(340);
        searchField.setPrefHeight(38);

        ComboBox<String> catFilter = new ComboBox<>();
        catFilter.getItems().addAll("All Categories",
            "Technology","Business","Arts & Culture",
            "Sports","Education","General");
        catFilter.setValue("All Categories");
        catFilter.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #cbd5e0;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-font-size: 13px;" +
            "-fx-pref-height: 38px;" +
            "-fx-pref-width: 200px;");

        HBox filterRow = new HBox(16, searchField, catFilter);
        filterRow.setAlignment(Pos.CENTER_LEFT);
        searchCard.getChildren().add(filterRow);

        // ── Events Table ──
        VBox tableCard = AdminUtils.card();
        Label tableTitle = AdminUtils.cardTitle("All My Events");

        Label actionMsg = new Label();
        actionMsg.setFont(Font.font("Arial", 13));

        TableView<ObservableList<String>> table =
            new TableView<>();
        table.setPrefHeight(420);
        table.setPlaceholder(
            new Label("No events yet. Create your first event!"));

        String[] cols   = {
            "Title","Date","Location","Category","Status"};
        int[]    widths = {200, 120, 160, 120, 90};
        for (int i = 0; i < cols.length; i++) {
            final int idx = i + 1;
            TableColumn<ObservableList<String>, String> col =
                new TableColumn<>(cols[i]);
            col.setCellValueFactory(d ->
                new SimpleStringProperty(
                    d.getValue().size() > idx
                    ? d.getValue().get(idx) : ""));
            col.setPrefWidth(widths[i]);
            table.getColumns().add(col);
        }

        // Edit column
        TableColumn<ObservableList<String>, Void> editCol =
            new TableColumn<>("Edit");
        editCol.setPrefWidth(80);
        editCol.setCellFactory(col -> new TableCell<>() {
            Button btn = new Button("Edit");
            {
                btn.setStyle(
                    "-fx-background-color: #4361ee;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 12px;" +
                    "-fx-background-radius: 5;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 4 10;");
                btn.setOnAction(e ->
                    showEditDialog(
                        getTableView().getItems()
                        .get(getIndex()),
                        table, controller,
                        organizerId, actionMsg));
            }
            @Override
            protected void updateItem(
                    Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
        table.getColumns().add(editCol);

        // Attendees column
        TableColumn<ObservableList<String>, Void> attCol =
            new TableColumn<>("Attendees");
        attCol.setPrefWidth(100);
        attCol.setCellFactory(col -> new TableCell<>() {
            Button btn = new Button("View");
            {
                btn.setStyle(
                    "-fx-background-color: #38a169;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 12px;" +
                    "-fx-background-radius: 5;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 4 10;");
                btn.setOnAction(e -> {
                    ObservableList<String> row =
                        getTableView().getItems()
                        .get(getIndex());
                    int eventId =
                        Integer.parseInt(row.get(0));
                    showAttendeesDialog(
                        eventId, row.get(1), controller);
                });
            }
            @Override
            protected void updateItem(
                    Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
        table.getColumns().add(attCol);

        // Delete column
        TableColumn<ObservableList<String>, Void> delCol =
            new TableColumn<>("Delete");
        delCol.setPrefWidth(90);
        delCol.setCellFactory(col -> new TableCell<>() {
            Button btn = new Button("Delete");
            {
                btn.setStyle(
                    "-fx-background-color: #e53e3e;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 12px;" +
                    "-fx-background-radius: 5;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 4 10;");
                btn.setOnAction(e -> {
                    ObservableList<String> row =
                        getTableView().getItems()
                        .get(getIndex());
                    int id = Integer.parseInt(row.get(0));
                    Alert alert = new Alert(
                        Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete Event");
                    alert.setHeaderText(
                        "Delete: " + row.get(1) + "?");
                    alert.setContentText(
                        "This cannot be undone.");
                    ButtonType yes = new ButtonType(
                        "Yes",
                        ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType(
                        "No",
                        ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(yes, no);
                    alert.showAndWait().ifPresent(res -> {
                        if (res == yes &&
                            controller.deleteEvent(id)) {
                            actionMsg.setText(
                                "✔  Event deleted.");
                            actionMsg.setTextFill(
                                Color.web("#38a169"));
                            loadTable(table, controller,
                                organizerId);
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
        table.getColumns().add(delCol);
        loadTable(table, controller, organizerId);

        // Search filter
        searchField.textProperty().addListener(
            (obs, old, nw) -> {
            if (nw == null || nw.isEmpty()) {
                loadTable(table, controller, organizerId);
            } else {
                ObservableList<ObservableList<String>>
                    filtered = FXCollections
                    .observableArrayList();
                for (String[] row : controller
                    .getEventsByOrganizerId(organizerId))
                    if (row[1].toLowerCase()
                           .contains(nw.toLowerCase()))
                        filtered.add(FXCollections
                            .observableArrayList(row));
                table.setItems(filtered);
            }
        });

        // Category filter
        catFilter.setOnAction(e -> {
            String cat = catFilter.getValue();
            if (cat.equals("All Categories")) {
                loadTable(table, controller, organizerId);
            } else {
                ObservableList<ObservableList<String>>
                    filtered = FXCollections
                    .observableArrayList();
                for (String[] row : controller
                    .getEventsByOrganizerId(organizerId))
                    if (row.length > 5 &&
                        row[5].equals(cat))
                        filtered.add(FXCollections
                            .observableArrayList(row));
                table.setItems(filtered);
            }
        });

        tableCard.getChildren().addAll(
            tableTitle, table, actionMsg);
        content.getChildren().addAll(
            headerRow, searchCard, tableCard);
        scroll.setContent(content);
        return AdminUtils.wrap(scroll);
    }

    // ── Edit Dialog ──
    private static void showEditDialog(
            ObservableList<String> row,
            TableView<ObservableList<String>> table,
            EventController controller,
            int organizerId, Label actionMsg) {

        Stage dialog = new Stage();
        dialog.setTitle("Edit Event");

        VBox layout = new VBox(14);
        layout.setPadding(new Insets(28));
        layout.setStyle("-fx-background-color: white;");

        Label title = new Label("Edit Event");
        title.setFont(Font.font(
            "Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#1b2a4a"));

        GridPane grid = new GridPane();
        grid.setHgap(16); grid.setVgap(10);

        TextField titleF = dField("Event Title");
        titleF.setText(row.get(1));
        TextField dateF  = dField("Date");
        dateF.setText(row.get(2));
        TextField locF   = dField("Location");
        locF.setText(row.get(3));
        TextField descF  = dField("Description");
        descF.setText(row.size() > 4 ? row.get(4) : "");
        TextField priceF = dField("Price");
        priceF.setText(row.size() > 6 ? row.get(6) : "");
        TextField capF   = dField("Capacity");
        capF.setText(row.size() > 7 ? row.get(7) : "100");

        ComboBox<String> catBox = new ComboBox<>();
        catBox.getItems().addAll("General","Technology",
            "Business","Arts & Culture","Sports","Education");
        catBox.setValue(row.size() > 5 &&
            !row.get(5).isEmpty() ? row.get(5) : "General");
        catBox.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #cbd5e0;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-pref-height: 38px;");
        catBox.setMaxWidth(Double.MAX_VALUE);

        grid.add(dLabel("Event Title"),  0, 0);
        grid.add(titleF,                 0, 1);
        grid.add(dLabel("Date"),         1, 0);
        grid.add(dateF,                  1, 1);
        grid.add(dLabel("Location"),     0, 2);
        grid.add(locF,                   0, 3);
        grid.add(dLabel("Description"),  1, 2);
        grid.add(descF,                  1, 3);
        grid.add(dLabel("Category"),     0, 4);
        grid.add(catBox,                 0, 5);
        grid.add(dLabel("Price"),        1, 4);
        grid.add(priceF,                 1, 5);
        grid.add(dLabel("Capacity"),     0, 6);
        grid.add(capF,                   0, 7);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(50);
        grid.getColumnConstraints().addAll(cc, cc);

        Button saveBtn = new Button("Update Event");
        saveBtn.setStyle(LoginView.primaryBtnStyle());
        saveBtn.setPrefHeight(40);
        saveBtn.setPrefWidth(200);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle(
            "-fx-background-color: #e2e8f0;" +
            "-fx-text-fill: #1b2a4a;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 24;");
        cancelBtn.setOnAction(e -> dialog.close());

        Label msg = new Label();
        msg.setFont(Font.font("Arial", 13));

        saveBtn.setOnAction(e -> {
            int cap = 100;
            try { cap = Integer.parseInt(
                capF.getText().trim()); }
            catch (Exception ex) { cap = 100; }

            boolean ok = controller.updateFullEvent(
                Integer.parseInt(row.get(0)),
                titleF.getText().trim(),
                dateF.getText().trim(),
                locF.getText().trim(),
                descF.getText().trim(),
                catBox.getValue(),
                priceF.getText().trim(),
                cap);
            if (ok) {
                actionMsg.setText(
                    "✔  Event updated successfully!");
                actionMsg.setTextFill(
                    Color.web("#38a169"));
                loadTable(table, controller, organizerId);
                dialog.close();
            } else {
                msg.setText("✘  Update failed.");
                msg.setTextFill(Color.web("#e53e3e"));
            }
        });

        layout.getChildren().addAll(
            title, grid,
            new HBox(12, saveBtn, cancelBtn), msg);
        dialog.setScene(new Scene(layout, 540, 500));
        dialog.show();
    }

    // ── Attendees Dialog ──
    private static void showAttendeesDialog(
            int eventId, String eventName,
            EventController controller) {

        Stage dialog = new Stage();
        dialog.setTitle("Attendees - " + eventName);

        VBox layout = new VBox(16);
        layout.setPadding(new Insets(28));
        layout.setStyle("-fx-background-color: white;");
        layout.setPrefWidth(500);

        Label title = new Label(
            "Attendees: " + eventName);
        title.setFont(Font.font(
            "Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#1b2a4a"));
        title.setWrapText(true);

        Separator sep = new Separator();

        TableView<ObservableList<String>> attTable =
            new TableView<>();
        attTable.setPrefHeight(300);
        attTable.setPlaceholder(
            new Label("No attendees registered yet"));

        String[] cols   = {"#","Username","Email","Registered At"};
        int[]    widths = {40, 140, 180, 130};
        attTable.getColumns().add(serialCol());
        for (int i = 1; i < cols.length; i++) {
            final int idx = i - 1;
            TableColumn<ObservableList<String>, String> col =
                new TableColumn<>(cols[i]);
            col.setCellValueFactory(d ->
                new SimpleStringProperty(
                    d.getValue().get(idx)));
            col.setPrefWidth(widths[i]);
            attTable.getColumns().add(col);
        }

        List<String[]> attendees =
            controller.getAttendeesByEvent(eventId);
        ObservableList<ObservableList<String>> data =
            FXCollections.observableArrayList();
        for (String[] row : attendees)
            data.add(FXCollections.observableArrayList(row));
        attTable.setItems(data);

        Label countLabel = new Label(
            "Total Attendees: " + attendees.size());
        countLabel.setFont(Font.font(
            "Arial", FontWeight.BOLD, 13));
        countLabel.setTextFill(Color.web("#4361ee"));

        Button closeBtn = new Button("Close");
        closeBtn.setStyle(LoginView.primaryBtnStyle());
        closeBtn.setPrefWidth(460);
        closeBtn.setPrefHeight(40);
        closeBtn.setOnAction(e -> dialog.close());

        layout.getChildren().addAll(
            title, sep, countLabel, attTable, closeBtn);
        dialog.setScene(new Scene(layout, 520, 460));
        dialog.show();
    }

    private static TableColumn<ObservableList<String>,
            String> serialCol() {
        TableColumn<ObservableList<String>, String> col =
            new TableColumn<>("#");
        col.setPrefWidth(40);
        col.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(
                    String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null :
                    String.valueOf(getIndex() + 1));
            }
        });
        return col;
    }

    static void loadTable(
            TableView<ObservableList<String>> table,
            EventController controller, int organizerId) {
        ObservableList<ObservableList<String>> data =
            FXCollections.observableArrayList();
        for (String[] row :
            controller.getEventsByOrganizerId(organizerId))
            data.add(FXCollections.observableArrayList(row));
        table.setItems(data);
    }

    private static TextField dField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setStyle(
            "-fx-background-color: #f8f9fa;" +
            "-fx-border-color: #cbd5e0;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 8 12;" +
            "-fx-font-size: 13px;");
        f.setMaxWidth(Double.MAX_VALUE);
        f.setPrefHeight(36);
        return f;
    }

    private static Label dLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        l.setTextFill(Color.web("#2d3748"));
        return l;
    }
}