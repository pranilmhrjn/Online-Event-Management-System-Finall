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
import javafx.stage.Screen;
import javafx.stage.Stage;

public class EventView {

    public static void show(Stage stage, String username, String role) {

        EventController controller = new EventController();
        HBox navbar  = AdminDashboardView.buildNavbar(username, LoginView.capitalize(role), stage);
        VBox sidebar = AdminDashboardView.buildSidebar(stage, username, role, "events");

        VBox content = new VBox(24);
        content.setPadding(new Insets(40));
        content.setStyle("-fx-background-color: #f0f2f7;");
        HBox.setHgrow(content, Priority.ALWAYS);
        VBox.setVgrow(content, Priority.ALWAYS);

        Label pageTitle = new Label("Manage Events");
        pageTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        pageTitle.setTextFill(Color.web("#1b2a4a"));

        VBox formCard = AdminUtils.card();
        Label formTitle = AdminUtils.cardTitle("Add New Event");

        GridPane grid = new GridPane();
        grid.setHgap(16); grid.setVgap(10);

        TextField titleField    = AdminUtils.field("Event Title");
        TextField dateField     = AdminUtils.field("Date (e.g. 2026-06-15)");
        TextField locationField = AdminUtils.field("Location");
        TextField descField     = AdminUtils.field("Description");

        grid.add(AdminUtils.lbl("Event Title"),  0, 0); grid.add(titleField,    0, 1);
        grid.add(AdminUtils.lbl("Date"),         1, 0); grid.add(dateField,     1, 1);
        grid.add(AdminUtils.lbl("Location"),     0, 2); grid.add(locationField, 0, 3);
        grid.add(AdminUtils.lbl("Description"),  1, 2); grid.add(descField,     1, 3);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(50);
        grid.getColumnConstraints().addAll(cc, cc);

        Button addBtn = new Button("➕   Add Event");
        addBtn.setStyle(LoginView.primaryBtnStyle());
        addBtn.setPrefHeight(42); addBtn.setPrefWidth(160);

        Label message = new Label();
        message.setFont(Font.font("Arial", 13));
        formCard.getChildren().addAll(formTitle, grid, addBtn, message);

        VBox tableCard = AdminUtils.card();
        tableCard.getChildren();
        Label tableTitle = AdminUtils.cardTitle("All Events");

        TableView<ObservableList<String>> table = new TableView<>();
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setPlaceholder(new Label("No events yet. Add your first event!"));

        String[] cols  = {"ID","Title","Date","Location","Description"};
        int[]    widths = {50, 200, 130, 180, 250};
        for (int i = 0; i < cols.length; i++) {
            final int idx = i;
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(cols[i]);
            col.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(idx)));
            col.setPrefWidth(widths[i]);
            table.getColumns().add(col);
        }

        TableColumn<ObservableList<String>, Void> deleteCol = new TableColumn<>("Action");
        deleteCol.setPrefWidth(100);
        deleteCol.setCellFactory(col -> new TableCell<>() {
            Button btn = new Button("Delete");
            {
                btn.setStyle("-fx-background-color: #e53e3e;-fx-text-fill: white;-fx-font-size: 12px;-fx-background-radius: 5;-fx-cursor: hand;-fx-padding: 5 12;");
                btn.setOnAction(e -> {
                    ObservableList<String> row = getTableView().getItems().get(getIndex());
                    int id = Integer.parseInt(row.get(0));
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Delete Event");
                    confirm.setHeaderText("Delete: " + row.get(1) + "?");
                    confirm.setContentText("Are you sure?");
                    ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no  = new ButtonType("No",  ButtonBar.ButtonData.CANCEL_CLOSE);
                    confirm.getButtonTypes().setAll(yes, no);
                    confirm.showAndWait().ifPresent(result -> {
                        if (result == yes && controller.deleteEvent(id)) {
                            loadTable(table, controller);
                            message.setText("✔ Event deleted.");
                            message.setTextFill(Color.web("#38a169"));
                        }
                    });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
        table.getColumns().add(deleteCol);
        loadTable(table, controller);

        addBtn.setOnAction(e -> {
            boolean ok = controller.addEvent(
                titleField.getText().trim(),
                dateField.getText().trim(),
                locationField.getText().trim(),
                descField.getText().trim());
            if (ok) {
                message.setText("✔ Event added successfully!");
                message.setTextFill(Color.web("#38a169"));
                titleField.clear(); dateField.clear();
                locationField.clear(); descField.clear();
                loadTable(table, controller);
            } else {
                message.setText("⚠ Please fill Title, Date and Location.");
                message.setTextFill(Color.web("#e53e3e"));
            }
        });

        tableCard.getChildren().addAll(tableTitle, table);
        content.getChildren().addAll(pageTitle, formCard, tableCard);

        HBox body = new HBox(sidebar, content);
        VBox.setVgrow(body, Priority.ALWAYS);
        VBox root = new VBox(navbar, body);

        AdminDashboardView.setScene(stage, root, EventView.class, "Events - EventPro");
    }

    private static void loadTable(TableView<ObservableList<String>> table, EventController controller) {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        for (String[] row : controller.getAllEvents())
            data.add(FXCollections.observableArrayList(row));
        table.setItems(data);
    }
}
