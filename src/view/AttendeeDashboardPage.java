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

public class AttendeeDashboardPage {

    public static VBox build(
            Stage stage, String username,
            int userId, StackPane contentArea,
            Button dashBtn, Button browseBtn,
            Button myRegBtn, Button profileBtn,
            Button passBtn) {

        ScrollPane scroll = AdminUtils.scroll();
        VBox content = AdminUtils.content();

        Label pageTitle =
            AdminUtils.pageTitle("Dashboard");

        VBox welcomeCard =
            AdminDashboardView.welcomeCard(
            "Welcome, " + username + "!",
            "Discover and register for " +
            "amazing events near you.");

        EventController ec = new EventController();
        List<String[]> allEvents =
            ec.getAllEventsDetailed();
        List<String[]> myRegs =
            ec.getRegisteredEvents(userId);

        // ── Stat Cards ──
        HBox statsRow = new HBox(20);
        statsRow.getChildren().addAll(
            AdminUtils.statCard(
                "Total Events",
                String.valueOf(allEvents.size()),
                "#4361ee", "Available events"),
            AdminUtils.statCard(
                "My Registrations",
                String.valueOf(myRegs.size()),
                "#38a169", "Registered events"),
            AdminUtils.statCard(
                "To Explore",
                String.valueOf(
                    allEvents.size() -
                    myRegs.size()),
                "#e67e22", "Events to register"),
            AdminUtils.statCard(
                "System Status",
                "Online", "#2dc653",
                "All services running"));

        // ── Quick Action Buttons ──
        HBox actionsRow = new HBox(16);
        Button quickBrowse =
            AttendeeUtils.actionBtn(
            "🔍  Browse Events", "#4361ee");
        Button quickMyReg =
            AttendeeUtils.actionBtn(
            "📋  My Registrations", "#38a169");
        Button quickProfile =
            AttendeeUtils.actionBtn(
            "👤  My Profile", "#9b59b6");
        Button quickPass =
            AttendeeUtils.actionBtn(
            "🔒  Change Password", "#e67e22");

        quickBrowse.setOnAction(e -> {
            AttendeeUtils.setActive(browseBtn,
                dashBtn, myRegBtn,
                profileBtn, passBtn);
            contentArea.getChildren().setAll(
                AttendeeBrowsePage.build(stage,
                    username, userId));
        });
        quickMyReg.setOnAction(e -> {
            AttendeeUtils.setActive(myRegBtn,
                dashBtn, browseBtn,
                profileBtn, passBtn);
            contentArea.getChildren().setAll(
                AttendeeMyEventsPage.build(stage,
                    username, userId));
        });
        quickProfile.setOnAction(e -> {
            AttendeeUtils.setActive(profileBtn,
                dashBtn, browseBtn,
                myRegBtn, passBtn);
            contentArea.getChildren().setAll(
                AttendeeProfilePage.build(stage,
                    username, userId));
        });
        quickPass.setOnAction(e -> {
            AttendeeUtils.setActive(passBtn,
                dashBtn, browseBtn,
                myRegBtn, profileBtn);
            contentArea.getChildren().setAll(
                AttendeePasswordPage.build(stage,
                    username, userId));
        });

        actionsRow.getChildren().addAll(
            quickBrowse, quickMyReg,
            quickProfile, quickPass);

        // ── Recent Events Table ──
        VBox recentCard = AdminUtils.card();
        Label recentTitle =
            AdminUtils.cardTitle(
            "Recently Added Events");

        TableView<ObservableList<String>> table =
            new TableView<>();
        table.setPrefHeight(220);
        table.setPlaceholder(new Label(
            "No events available yet"));

        String[] cols   = {
            "Event Name", "Date",
            "Location", "Category", "Price"};
        int[]    widths = {
            220, 120, 160, 120, 90};
        int[]    idxs   = {1, 2, 3, 5, 6};

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

        ObservableList<ObservableList<String>>
            data = FXCollections
            .observableArrayList();
        for (String[] row : allEvents)
            data.add(FXCollections
                .observableArrayList(row));
        table.setItems(data);

        // My Registrations Table
        VBox myRegCard = AdminUtils.card();
        Label myRegTitle =
            AdminUtils.cardTitle(
            "My Registered Events");

        TableView<ObservableList<String>> regTable =
            new TableView<>();
        regTable.setPrefHeight(180);
        regTable.setPlaceholder(new Label(
            "You have not registered yet."));

        String[] rCols  = {
            "Event Name", "Date",
            "Location", "Category"};
        int[]    rWidths = {220, 120, 160, 120};
        int[]    rIdxs   = {1, 2, 3, 5};

        for (int i = 0; i < rCols.length; i++) {
            final int idx = rIdxs[i];
            TableColumn<ObservableList<String>,
                String> col =
                new TableColumn<>(rCols[i]);
            col.setCellValueFactory(d ->
                new SimpleStringProperty(
                    d.getValue().size() > idx
                    ? d.getValue().get(idx) : ""));
            col.setPrefWidth(rWidths[i]);
            regTable.getColumns().add(col);
        }

        ObservableList<ObservableList<String>>
            regData = FXCollections
            .observableArrayList();
        for (String[] row : myRegs)
            regData.add(FXCollections
                .observableArrayList(row));
        regTable.setItems(regData);

        recentCard.getChildren().addAll(
            recentTitle, table);
        myRegCard.getChildren().addAll(
            myRegTitle, regTable);

        content.getChildren().addAll(
            pageTitle, welcomeCard,
            statsRow, actionsRow,
            recentCard, myRegCard);
        scroll.setContent(content);
        return AdminUtils.wrap(scroll);
    }
}