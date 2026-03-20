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

public class AttendeeBrowsePage {

    public static VBox build(Stage stage,
            String username, int userId) {

        EventController ec = new EventController();
        ScrollPane scroll = AdminUtils.scroll();
        VBox content = AdminUtils.content();

        Label pageTitle =
            AdminUtils.pageTitle("Browse Events");

        // ── Search Card ──
        VBox searchCard = AdminUtils.card();
        Label searchTitle =
            AdminUtils.cardTitle(
            "Search & Filter Events");

        TextField keywordField = new TextField();
        keywordField.setPromptText(
            "Search by event name...");
        keywordField.setStyle(
            LoginView.inputStyle());
        keywordField.setPrefWidth(260);
        keywordField.setPrefHeight(38);

        ComboBox<String> catFilter =
            new ComboBox<>();
        catFilter.getItems().addAll(
            "All Categories", "Technology",
            "Business", "Arts & Culture",
            "Sports", "Education", "General");
        catFilter.setValue("All Categories");
        catFilter.setStyle(
            AttendeeUtils.comboStyle());

        TextField dateField = new TextField();
        dateField.setPromptText(
            "Date (2026-06-15)");
        dateField.setStyle(LoginView.inputStyle());
        dateField.setPrefWidth(180);
        dateField.setPrefHeight(38);

        Button searchBtn =
            new Button("🔍  Search");
        searchBtn.setStyle(
            "-fx-background-color: #4361ee;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 20;");

        Button clearBtn = new Button("Clear");
        clearBtn.setStyle(
            "-fx-background-color: #e2e8f0;" +
            "-fx-text-fill: #1b2a4a;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 16;");

        HBox filterRow = new HBox(12,
            keywordField, catFilter,
            dateField, searchBtn, clearBtn);
        filterRow.setAlignment(Pos.CENTER_LEFT);
        searchCard.getChildren().addAll(
            searchTitle, filterRow);

        // ── Table Card ──
        VBox tableCard = AdminUtils.card();
        Label tableTitle = AdminUtils.cardTitle(
            "All Available Events");

        Label actionMsg =
            AttendeeUtils.msgLabel();

        TableView<ObservableList<String>> table =
            new TableView<>();
        table.setPrefHeight(450);
        table.setPlaceholder(
            new Label("No events found."));

        String[] cols   = {
            "Event Name", "Date", "Location",
            "Category", "Price", "Seats Left"};
        int[]    widths = {
            200, 120, 160, 120, 90, 90};

        for (int i = 0; i < cols.length; i++) {
            final int i2 = i;
            TableColumn<ObservableList<String>,
                String> col =
                new TableColumn<>(cols[i]);
            col.setCellValueFactory(d -> {
                ObservableList<String> row =
                    d.getValue();
                if (i2 == 5) {
                    try {
                        int cap =
                            Integer.parseInt(
                            row.size() > 7
                            ? row.get(7) : "0");
                        int reg =
                            Integer.parseInt(
                            row.size() > 8
                            ? row.get(8) : "0");
                        return new
                            SimpleStringProperty(
                            String.valueOf(
                            Math.max(cap-reg, 0)));
                    } catch (Exception e) {
                        return new
                            SimpleStringProperty(
                            "?");
                    }
                }
                int[] idxMap =
                    {1, 2, 3, 5, 6, 7};
                return new SimpleStringProperty(
                    row.size() > idxMap[i2]
                    ? row.get(idxMap[i2]) : "");
            });
            col.setPrefWidth(widths[i]);
            table.getColumns().add(col);
        }

        // Register / Cancel column
        TableColumn<ObservableList<String>, Void>
            regCol = new TableColumn<>("Action");
        regCol.setPrefWidth(120);
        regCol.setCellFactory(
            col -> new TableCell<>() {
            Button btn = new Button();
            {
                btn.setPrefWidth(100);
                btn.setOnAction(e -> {
                    ObservableList<String> row =
                        getTableView().getItems()
                        .get(getIndex());
                    int eventId =
                        Integer.parseInt(
                        row.get(0));
                    boolean already =
                        ec.isRegistered(
                        userId, eventId);
                    if (already) {
                        Alert confirm = new Alert(
                            Alert.AlertType
                            .CONFIRMATION);
                        confirm.setTitle(
                            "Cancel Registration");
                        confirm.setHeaderText(
                            "Cancel: " +
                            row.get(1) + "?");
                        confirm.setContentText(
                            "Are you sure?");
                        ButtonType yes =
                            new ButtonType("Yes",
                            ButtonBar.ButtonData
                            .OK_DONE);
                        ButtonType no =
                            new ButtonType("No",
                            ButtonBar.ButtonData
                            .CANCEL_CLOSE);
                        confirm.getButtonTypes()
                            .setAll(yes, no);
                        confirm.showAndWait()
                            .ifPresent(r -> {
                            if (r == yes) {
                                ec.cancelRegistration(
                                    userId, eventId);
                                AttendeeUtils
                                    .setSuccess(
                                    actionMsg,
                                    "✔ Cancelled: "
                                    + row.get(1));
                                loadTable(table,
                                    ec, "", 
                                    "All Categories",
                                    "");
                            }
                        });
                    } else {
                        boolean ok =
                            ec.registerForEvent(
                            userId, eventId);
                        if (ok) {
                            AttendeeUtils
                                .setSuccess(
                                actionMsg,
                                "✔ Registered: " +
                                row.get(1));
                        } else {
                            AttendeeUtils
                                .setError(
                                actionMsg,
                                "✘ Already " +
                                "registered.");
                        }
                        loadTable(table, ec,
                            "", "All Categories",
                            "");
                    }
                });
            }

            @Override
            protected void updateItem(
                    Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ObservableList<String> row =
                        getTableView().getItems()
                        .get(getIndex());
                    int eventId =
                        Integer.parseInt(
                        row.get(0));
                    boolean reg =
                        ec.isRegistered(
                        userId, eventId);
                    if (reg) {
                        btn.setText("✘ Cancel");
                        btn.setStyle(
                            "-fx-background-color:" +
                            " #e53e3e;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 12px;" +
                            "-fx-background-radius:" +
                            " 5;" +
                            "-fx-cursor: hand;" +
                            "-fx-padding: 5 10;");
                    } else {
                        btn.setText(
                            "✔ Register");
                        btn.setStyle(
                            "-fx-background-color:" +
                            " #38a169;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 12px;" +
                            "-fx-background-radius:" +
                            " 5;" +
                            "-fx-cursor: hand;" +
                            "-fx-padding: 5 10;");
                    }
                    setGraphic(btn);
                }
            }
        });
        table.getColumns().add(regCol);
        loadTable(table, ec,
            "", "All Categories", "");

        searchBtn.setOnAction(e ->
            loadTable(table, ec,
                keywordField.getText().trim(),
                catFilter.getValue(),
                dateField.getText().trim()));

        clearBtn.setOnAction(e -> {
            keywordField.clear();
            catFilter.setValue("All Categories");
            dateField.clear();
            loadTable(table, ec,
                "", "All Categories", "");
        });

        keywordField.setOnAction(e ->
            loadTable(table, ec,
                keywordField.getText().trim(),
                catFilter.getValue(),
                dateField.getText().trim()));

        tableCard.getChildren().addAll(
            tableTitle, table, actionMsg);
        content.getChildren().addAll(
            pageTitle, searchCard, tableCard);
        scroll.setContent(content);
        return AdminUtils.wrap(scroll);
    }

    static void loadTable(
            TableView<ObservableList<String>> table,
            EventController ec,
            String keyword, String category,
            String date) {
        ObservableList<ObservableList<String>>
            data = FXCollections
            .observableArrayList();
        for (String[] row :
            ec.searchEvents(keyword, category, date))
            data.add(FXCollections
                .observableArrayList(row));
        table.setItems(data);
        table.refresh();
    }
}