package view;

import controller.EventController;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class CreateEventView {

    public static VBox build(
            Stage stage, String username, int organizerId) {

        ScrollPane scroll = AdminUtils.scroll();
        VBox content = AdminUtils.content();

        Label pageTitle = AdminUtils.pageTitle("Create Event");

        VBox formCard = AdminUtils.card();
        Label formTitle = AdminUtils.cardTitle(
            "Fill in the event details below");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(14);

        // Row 0 - Event Name
        TextField nameField = formField("e.g. Tech Conference 2026");
        // Row 1 - Date + Time
        TextField dateField = formField("e.g. 2026-06-15");
        TextField timeField = formField("e.g. 09:00 AM");
        // Row 2 - Location + Category
        TextField locField  = formField("e.g. Convention Center");
        ComboBox<String> catBox = new ComboBox<>();
        catBox.getItems().addAll("General","Technology",
            "Business","Arts & Culture","Sports","Education");
        catBox.setValue("General");
        catBox.setStyle(comboStyle());
        catBox.setMaxWidth(Double.MAX_VALUE);
        // Row 3 - Price + Capacity
        TextField priceField = formField("e.g. $50 or Free");
        TextField capField   = formField("e.g. 100");
        // Row 4 - Description
        TextArea descArea = new TextArea();
        descArea.setPromptText("Describe your event...");
        descArea.setPrefRowCount(4);
        descArea.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #cbd5e0;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-font-size: 13px;");
        descArea.setMaxWidth(Double.MAX_VALUE);

        // Add to grid
        grid.add(formLabel("Event Name"),  0, 0);
        grid.add(nameField,                0, 1, 2, 1);
        grid.add(formLabel("Date"),        0, 2);
        grid.add(formLabel("Time"),        1, 2);
        grid.add(dateField,                0, 3);
        grid.add(timeField,                1, 3);
        grid.add(formLabel("Location"),    0, 4);
        grid.add(formLabel("Category"),    1, 4);
        grid.add(locField,                 0, 5);
        grid.add(catBox,                   1, 5);
        grid.add(formLabel("Price"),       0, 6);
        grid.add(formLabel("Capacity"),    1, 6);
        grid.add(priceField,               0, 7);
        grid.add(capField,                 1, 7);
        grid.add(formLabel("Description"), 0, 8);
        grid.add(descArea,                 0, 9, 2, 1);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(50);
        grid.getColumnConstraints().addAll(cc, cc);

        Button createBtn = new Button("Create Event");
        createBtn.setStyle(
            "-fx-background-color: #1b2a4a;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 12 30;");

        Button clearBtn = new Button("Clear Form");
        clearBtn.setStyle(
            "-fx-background-color: #e2e8f0;" +
            "-fx-text-fill: #1b2a4a;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 12 24;");

        HBox btnRow = new HBox(12, createBtn, clearBtn);

        Label msg = new Label();
        msg.setFont(Font.font("Arial", 13));
        msg.setWrapText(true);

        clearBtn.setOnAction(e -> {
            nameField.clear(); dateField.clear();
            timeField.clear(); locField.clear();
            priceField.clear(); capField.clear();
            descArea.clear(); catBox.setValue("General");
            msg.setText("");
        });

        createBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String date = dateField.getText().trim();
            String time = timeField.getText().trim();
            String loc  = locField.getText().trim();
            String desc = descArea.getText().trim();
            String cat  = catBox.getValue();
            String price = priceField.getText().trim();
            int cap = 100;
            try { cap = Integer.parseInt(
                capField.getText().trim()); }
            catch (Exception ex) { cap = 100; }

            if (name.isEmpty() || date.isEmpty() ||
                loc.isEmpty()) {
                msg.setText(
                    "⚠  Please fill Event Name, Date & Location.");
                msg.setTextFill(Color.web("#e53e3e"));
                return;
            }

            EventController controller = new EventController();
            boolean ok = controller.addEventWithOrganizer(
                name, date, time, loc, desc,
                cat, price, cap, organizerId, username);

            if (ok) {
                msg.setText(
                    "✔  Event created successfully!");
                msg.setTextFill(Color.web("#38a169"));
                nameField.clear(); dateField.clear();
                timeField.clear(); locField.clear();
                priceField.clear(); capField.clear();
                descArea.clear(); catBox.setValue("General");
            } else {
                msg.setText("✘  Failed to create event.");
                msg.setTextFill(Color.web("#e53e3e"));
            }
        });

        formCard.getChildren().addAll(
            formTitle, grid, btnRow, msg);
        content.getChildren().addAll(pageTitle, formCard);
        scroll.setContent(content);
        return AdminUtils.wrap(scroll);
    }

    private static TextField formField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #cbd5e0;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 10 14;" +
            "-fx-font-size: 13px;");
        f.setMaxWidth(Double.MAX_VALUE);
        f.setPrefHeight(40);
        return f;
    }

    private static Label formLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        l.setTextFill(Color.web("#2d3748"));
        return l;
    }

    private static String comboStyle() {
        return "-fx-background-color: white;" +
               "-fx-border-color: #cbd5e0;" +
               "-fx-border-radius: 6;" +
               "-fx-background-radius: 6;" +
               "-fx-font-size: 13px;" +
               "-fx-pref-height: 40px;";
    }
}