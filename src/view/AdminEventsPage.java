package view;

import controller.EventController;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class AdminEventsPage {

    public static VBox build() {

        ScrollPane scroll = AdminUtils.scroll();
        VBox content = AdminUtils.content();
        EventController controller = new EventController();

        Label pageTitle = AdminUtils.pageTitle("Event Management");

        VBox formCard = AdminUtils.card();
        Label formTitle = AdminUtils.cardTitle("Add New Event");

        GridPane grid = new GridPane();
        grid.setHgap(16); grid.setVgap(10);

        TextField titleF = AdminUtils.field("Event Title");
        TextField dateF  = AdminUtils.field("Date (e.g. 2026-06-15)");
        TextField locF   = AdminUtils.field("Location");
        TextField descF  = AdminUtils.field("Description");

        grid.add(AdminUtils.lbl("Event Title"), 0, 0); grid.add(titleF, 0, 1);
        grid.add(AdminUtils.lbl("Date"),        1, 0); grid.add(dateF,  1, 1);
        grid.add(AdminUtils.lbl("Location"),    0, 2); grid.add(locF,   0, 3);
        grid.add(AdminUtils.lbl("Description"), 1, 2); grid.add(descF,  1, 3);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(50);
        grid.getColumnConstraints().addAll(cc, cc);

        Button addBtn = new Button("+ Add Event");
        addBtn.setStyle(LoginView.primaryBtnStyle());
        addBtn.setPrefHeight(40); addBtn.setPrefWidth(150);

        Label formMsg = new Label();
        formMsg.setFont(Font.font("Arial", 13));
        formCard.getChildren().addAll(formTitle, grid, addBtn, formMsg);

        VBox tableCard = AdminUtils.card();
        Label tableTitle = AdminUtils.cardTitle("All Events");

        TextField searchField = new TextField();
        searchField.setPromptText("Search events...");
        searchField.setStyle(LoginView.inputStyle());
        searchField.setPrefWidth(280); searchField.setPrefHeight(36);

        TableView<ObservableList<String>> table = new TableView<>();
        table.setPrefHeight(350);
        table.setPlaceholder(new Label("No events yet!"));

        String[] cols = {"ID","Title","Date","Location","Description"};
        int[]    widths = {50, 180, 120, 160, 200};
        for (int i = 0; i < cols.length; i++) {
            final int idx = i;
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(cols[i]);
            col.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().get(idx)));
            col.setPrefWidth(widths[i]);
            table.getColumns().add(col);
        }

        TableColumn<ObservableList<String>, Void> editCol = new TableColumn<>("Edit");
        editCol.setPrefWidth(80);
        editCol.setCellFactory(col -> new TableCell<>() {
            Button btn = new Button("Edit");
            { btn.setStyle("-fx-background-color: #4361ee;-fx-text-fill: white;-fx-font-size: 12px;-fx-background-radius: 5;-fx-cursor: hand;-fx-padding: 4 10;");
              btn.setOnAction(e -> showEditDialog(getTableView().getItems().get(getIndex()), table, controller, formMsg)); }
            @Override protected void updateItem(Void item, boolean empty) { super.updateItem(item, empty); setGraphic(empty ? null : btn); }
        });
        table.getColumns().add(editCol);

        TableColumn<ObservableList<String>, Void> delCol = new TableColumn<>("Delete");
        delCol.setPrefWidth(90);
        delCol.setCellFactory(col -> new TableCell<>() {
            Button btn = new Button("Delete");
            { btn.setStyle("-fx-background-color: #e53e3e;-fx-text-fill: white;-fx-font-size: 12px;-fx-background-radius: 5;-fx-cursor: hand;-fx-padding: 4 10;");
              btn.setOnAction(e -> {
                ObservableList<String> row = getTableView().getItems().get(getIndex());
                int id = Integer.parseInt(row.get(0));
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Event"); alert.setHeaderText("Delete: " + row.get(1) + "?"); alert.setContentText("This cannot be undone.");
                ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType no  = new ButtonType("No",  ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(yes, no);
                alert.showAndWait().ifPresent(res -> { if (res == yes && controller.deleteEvent(id)) { loadTable(table, controller); formMsg.setText("Event deleted."); formMsg.setTextFill(Color.web("#38a169")); } });
              }); }
            @Override protected void updateItem(Void item, boolean empty) { super.updateItem(item, empty); setGraphic(empty ? null : btn); }
        });
        table.getColumns().add(delCol);
        loadTable(table, controller);

        searchField.textProperty().addListener((o, v, n) -> {
            if (n == null || n.isEmpty()) { loadTable(table, controller); }
            else {
                ObservableList<ObservableList<String>> filtered = FXCollections.observableArrayList();
                for (String[] row : controller.getAllEvents())
                    if (row[1].toLowerCase().contains(n.toLowerCase()))
                        filtered.add(FXCollections.observableArrayList(row));
                table.setItems(filtered);
            }
        });

        addBtn.setOnAction(e -> {
            boolean ok = controller.addEvent(titleF.getText().trim(), dateF.getText().trim(), locF.getText().trim(), descF.getText().trim());
            if (ok) { formMsg.setText("Event added!"); formMsg.setTextFill(Color.web("#38a169")); titleF.clear(); dateF.clear(); locF.clear(); descF.clear(); loadTable(table, controller); }
            else { formMsg.setText("Fill Title, Date & Location."); formMsg.setTextFill(Color.web("#e53e3e")); }
        });

        tableCard.getChildren().addAll(tableTitle, searchField, table);
        content.getChildren().addAll(pageTitle, formCard, tableCard);
        scroll.setContent(content);
        return AdminUtils.wrap(scroll);
    }

    private static void showEditDialog(ObservableList<String> row,
            TableView<ObservableList<String>> table,
            EventController controller, Label formMsg) {
        Stage dialog = new Stage();
        dialog.setTitle("Edit Event");

        VBox layout = new VBox(14);
        layout.setPadding(new Insets(28));
        layout.setStyle("-fx-background-color: white;");

        Label title = new Label("Edit Event");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#1b2a4a"));

        GridPane grid = new GridPane();
        grid.setHgap(16); grid.setVgap(10);

        TextField titleF = AdminUtils.field("Event Title"); titleF.setText(row.get(1));
        TextField dateF  = AdminUtils.field("Date");        dateF.setText(row.get(2));
        TextField locF   = AdminUtils.field("Location");    locF.setText(row.get(3));
        TextField descF  = AdminUtils.field("Description"); descF.setText(row.size() > 4 ? row.get(4) : "");

        grid.add(AdminUtils.lbl("Event Title"), 0, 0); grid.add(titleF, 0, 1);
        grid.add(AdminUtils.lbl("Date"),        1, 0); grid.add(dateF,  1, 1);
        grid.add(AdminUtils.lbl("Location"),    0, 2); grid.add(locF,   0, 3);
        grid.add(AdminUtils.lbl("Description"), 1, 2); grid.add(descF,  1, 3);

        ColumnConstraints cc = new ColumnConstraints(); cc.setPercentWidth(50); grid.getColumnConstraints().addAll(cc, cc);

        Button saveBtn = new Button("Update Event"); saveBtn.setStyle(LoginView.primaryBtnStyle()); saveBtn.setPrefHeight(40); saveBtn.setPrefWidth(200);
        Button cancelBtn = new Button("Cancel"); cancelBtn.setStyle("-fx-background-color: #e2e8f0;-fx-text-fill: #1b2a4a;-fx-font-size: 13px;-fx-background-radius: 8;-fx-cursor: hand;-fx-padding: 10 24;");
        cancelBtn.setOnAction(e -> dialog.close());

        HBox btnRow = new HBox(12, saveBtn, cancelBtn);
        Label msg = new Label(); msg.setFont(Font.font("Arial", 13));

        saveBtn.setOnAction(e -> {
            boolean ok = controller.updateEvent(Integer.parseInt(row.get(0)), titleF.getText().trim(), dateF.getText().trim(), locF.getText().trim(), descF.getText().trim());
            if (ok) { formMsg.setText("Event updated!"); formMsg.setTextFill(Color.web("#38a169")); loadTable(table, controller); dialog.close(); }
            else { msg.setText("Update failed."); msg.setTextFill(Color.web("#e53e3e")); }
        });

        layout.getChildren().addAll(title, grid, btnRow, msg);
        dialog.setScene(new Scene(layout, 520, 380));
        dialog.show();
    }

    static void loadTable(TableView<ObservableList<String>> table, EventController controller) {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        for (String[] row : controller.getAllEvents())
            data.add(FXCollections.observableArrayList(row));
        table.setItems(data);
    }
}
