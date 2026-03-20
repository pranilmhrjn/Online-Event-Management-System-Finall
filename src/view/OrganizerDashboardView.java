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
import model.UserModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class OrganizerDashboardView {

    public static void show(
            Stage stage, String username) {

        int organizerId =
            new UserModel().getUserId(username);

        HBox navbar = AdminDashboardView
            .buildNavbar(username, "Organizer",
                stage);

        // ── Sidebar ──
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(240);
        sidebar.setStyle(
            "-fx-background-color: #1b2a4a;");

        VBox sidebarHeader = new VBox(4);
        sidebarHeader.setPadding(
            new Insets(24, 20, 20, 24));
        sidebarHeader.setStyle(
            "-fx-background-color: #162236;");

        Label portalLabel =
            new Label("Organizer Portal");
        portalLabel.setFont(Font.font(
            "Arial", FontWeight.BOLD, 16));
        portalLabel.setTextFill(Color.WHITE);

        Label subLabel =
            new Label("Event Management");
        subLabel.setFont(Font.font("Arial", 12));
        subLabel.setTextFill(Color.web("#a0aec0"));
        sidebarHeader.getChildren().addAll(
            portalLabel, subLabel);

        Button dashBtn = AdminDashboardView
            .sidebarBtn("  Dashboard", true);
        Button createBtn = AdminDashboardView
            .sidebarBtn("  Create Event", false);
        Button myEventsBtn = AdminDashboardView
            .sidebarBtn("  My Events", false);
        Button attendeesBtn = AdminDashboardView
            .sidebarBtn("  View Attendees", false);

        VBox sidebarMenu = new VBox(4, dashBtn,
            createBtn, myEventsBtn, attendeesBtn);
        sidebarMenu.setPadding(
            new Insets(16, 0, 0, 0));
        VBox.setVgrow(sidebarMenu, Priority.ALWAYS);

        Button logoutBtn = new Button("  Logout");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setAlignment(Pos.CENTER_LEFT);
        logoutBtn.setPadding(
            new Insets(12, 20, 12, 24));
        logoutBtn.setFont(Font.font("Arial", 14));
        logoutBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #e53e3e;" +
            "-fx-cursor: hand;" +
            "-fx-background-radius: 0;");
        logoutBtn.setOnAction(
            e -> EntryView.show(stage));

        VBox logoutBox = new VBox(logoutBtn);
        logoutBox.setPadding(
            new Insets(0, 0, 20, 0));
        sidebar.getChildren().addAll(
            sidebarHeader, sidebarMenu, logoutBox);

        // ── Content Area ──
        StackPane contentArea = new StackPane();
        HBox.setHgrow(contentArea, Priority.ALWAYS);
        contentArea.getChildren().add(
            buildDashboard(stage, username,
                organizerId, contentArea,
                dashBtn, createBtn,
                myEventsBtn, attendeesBtn));

        dashBtn.setOnAction(e -> {
            setActive(dashBtn, createBtn,
                myEventsBtn, attendeesBtn);
            contentArea.getChildren().setAll(
                buildDashboard(stage, username,
                    organizerId, contentArea,
                    dashBtn, createBtn,
                    myEventsBtn, attendeesBtn));
        });
        createBtn.setOnAction(e -> {
            setActive(createBtn, dashBtn,
                myEventsBtn, attendeesBtn);
            contentArea.getChildren().setAll(
                CreateEventView.build(stage,
                    username, organizerId));
        });
        myEventsBtn.setOnAction(e -> {
            setActive(myEventsBtn, dashBtn,
                createBtn, attendeesBtn);
            contentArea.getChildren().setAll(
                OrganizerEventsPage.build(stage,
                    username, organizerId));
        });
        attendeesBtn.setOnAction(e -> {
            setActive(attendeesBtn, dashBtn,
                createBtn, myEventsBtn);
            contentArea.getChildren().setAll(
                ViewAttendeesPage.build(stage,
                    username, organizerId));
        });

        HBox body = new HBox(sidebar, contentArea);
        VBox.setVgrow(body, Priority.ALWAYS);
        VBox root = new VBox(navbar, body);

        AdminDashboardView.setScene(stage, root,
            OrganizerDashboardView.class,
            "Organizer Dashboard - EventPro");
    }

    // ══════════════════════════════════════
    // DASHBOARD PAGE
    // ══════════════════════════════════════
    private static VBox buildDashboard(
            Stage stage, String username,
            int organizerId,
            StackPane contentArea,
            Button dashBtn, Button createBtn,
            Button myEventsBtn,
            Button attendeesBtn) {

        ScrollPane scroll = AdminUtils.scroll();
        VBox content = AdminUtils.content();

        // ── Page Title ──
        Label pageTitle =
            AdminUtils.pageTitle("Dashboard");

        // ── Welcome Card ──
        VBox welcomeCard =
            AdminDashboardView.welcomeCard(
            "Welcome, " + username + "!",
            "Here is a summary of your events " +
            "— past and upcoming.");

        // ── Load Events ──
        EventController ec = new EventController();
        List<String[]> allEvents =
            ec.getEventsByOrganizerId(organizerId);

        // Separate past and upcoming
        List<String[]> pastEvents = new ArrayList<>();
        List<String[]> upcomingEvents =
            new ArrayList<>();

        String today = LocalDate.now().toString();
        for (String[] ev : allEvents) {
            String evDate = ev[2];
            try {
                if (evDate.compareTo(today) < 0)
                    pastEvents.add(ev);
                else
                    upcomingEvents.add(ev);
            } catch (Exception e) {
                upcomingEvents.add(ev);
            }
        }

        // ── Stat Cards ──
        HBox statsRow = new HBox(20);
        statsRow.getChildren().addAll(
            AdminUtils.statCard("Total Events",
                String.valueOf(allEvents.size()),
                "#4361ee", "All events"),
            AdminUtils.statCard("Past Events",
                String.valueOf(pastEvents.size()),
                "#718096", "Completed events"),
            AdminUtils.statCard("Upcoming Events",
                String.valueOf(
                    upcomingEvents.size()),
                "#38a169", "Scheduled events"),
            AdminUtils.statCard("System Status",
                "Online", "#2dc653",
                "All services running"));

        // ── Upcoming Events Section ──
        VBox upcomingCard = AdminUtils.card();

        HBox upcomingHeader = new HBox();
        upcomingHeader.setAlignment(
            Pos.CENTER_LEFT);
        Label upcomingTitle = new Label(
            "Upcoming Events");
        upcomingTitle.setFont(Font.font(
            "Arial", FontWeight.BOLD, 16));
        upcomingTitle.setTextFill(
            Color.web("#1b2a4a"));

        Region upSpacer = new Region();
        HBox.setHgrow(upSpacer, Priority.ALWAYS);

        Label upCount = new Label(
            upcomingEvents.size() + " events");
        upCount.setFont(Font.font("Arial", 13));
        upCount.setTextFill(Color.web("#38a169"));
        upCount.setStyle(
            "-fx-background-color: #e8f5e9;" +
            "-fx-padding: 4 12;" +
            "-fx-background-radius: 20;");

        upcomingHeader.getChildren().addAll(
            upcomingTitle, upSpacer, upCount);

        TableView<ObservableList<String>>
            upcomingTable = new TableView<>();
        upcomingTable.setPrefHeight(200);
        upcomingTable.setPlaceholder(new Label(
            "No upcoming events scheduled."));

        String[] upCols = {
            "Event Name", "Date", "Time",
            "Location", "Category", "Capacity"};
        int[] upWidths = {
            200, 120, 100, 160, 110, 90};
        int[] upIdxs = {1, 2, 3, 4, 5, 7};

        for (int i = 0; i < upCols.length; i++) {
            final int idx = upIdxs[i];
            TableColumn<ObservableList<String>,
                String> col =
                new TableColumn<>(upCols[i]);
            col.setCellValueFactory(d ->
                new SimpleStringProperty(
                    d.getValue().size() > idx
                    ? d.getValue().get(idx) : ""));
            col.setPrefWidth(upWidths[i]);
            upcomingTable.getColumns().add(col);
        }

        // Color upcoming rows green
        upcomingTable.setRowFactory(tv -> {
            TableRow<ObservableList<String>> row =
                new TableRow<>();
            row.itemProperty().addListener(
                (obs, old, nw) -> {
                if (nw != null) {
                    row.setStyle(
                        "-fx-background-color: " +
                        "#f0fff4;");
                } else {
                    row.setStyle("");
                }
            });
            return row;
        });

        ObservableList<ObservableList<String>>
            upData = FXCollections
            .observableArrayList();
        for (String[] row : upcomingEvents)
            upData.add(FXCollections
                .observableArrayList(row));
        upcomingTable.setItems(upData);

        upcomingCard.getChildren().addAll(
            upcomingHeader, upcomingTable);

        // ── Past Events Section ──
        VBox pastCard = AdminUtils.card();

        HBox pastHeader = new HBox();
        pastHeader.setAlignment(Pos.CENTER_LEFT);
        Label pastTitle =
            new Label("Past Events");
        pastTitle.setFont(Font.font(
            "Arial", FontWeight.BOLD, 16));
        pastTitle.setTextFill(
            Color.web("#1b2a4a"));

        Region pastSpacer = new Region();
        HBox.setHgrow(pastSpacer, Priority.ALWAYS);

        Label pastCount = new Label(
            pastEvents.size() + " events");
        pastCount.setFont(Font.font("Arial", 13));
        pastCount.setTextFill(
            Color.web("#718096"));
        pastCount.setStyle(
            "-fx-background-color: #f0f2f7;" +
            "-fx-padding: 4 12;" +
            "-fx-background-radius: 20;");

        pastHeader.getChildren().addAll(
            pastTitle, pastSpacer, pastCount);

        TableView<ObservableList<String>>
            pastTable = new TableView<>();
        pastTable.setPrefHeight(200);
        pastTable.setPlaceholder(new Label(
            "No past events yet."));

        String[] pastCols = {
            "Event Name", "Date", "Location",
            "Category", "Status"};
        int[] pastWidths = {
            220, 120, 180, 120, 100};
        int[] pastIdxs = {1, 2, 4, 5, 9};

        for (int i = 0; i < pastCols.length; i++) {
            final int idx = pastIdxs[i];
            TableColumn<ObservableList<String>,
                String> col =
                new TableColumn<>(pastCols[i]);
            col.setCellValueFactory(d ->
                new SimpleStringProperty(
                    d.getValue().size() > idx
                    ? d.getValue().get(idx) : ""));
            col.setPrefWidth(pastWidths[i]);
            pastTable.getColumns().add(col);
        }

        // Color past rows grey
        pastTable.setRowFactory(tv -> {
            TableRow<ObservableList<String>> row =
                new TableRow<>();
            row.itemProperty().addListener(
                (obs, old, nw) -> {
                if (nw != null) {
                    row.setStyle(
                        "-fx-background-color: " +
                        "#fafafa;");
                } else {
                    row.setStyle("");
                }
            });
            return row;
        });

        ObservableList<ObservableList<String>>
            pastData = FXCollections
            .observableArrayList();
        for (String[] row : pastEvents)
            pastData.add(FXCollections
                .observableArrayList(row));
        pastTable.setItems(pastData);

        pastCard.getChildren().addAll(
            pastHeader, pastTable);

        // ── Summary Info Bar ──
        HBox summaryBar = new HBox(20);
        summaryBar.setPadding(
            new Insets(16, 20, 16, 20));
        summaryBar.setAlignment(Pos.CENTER_LEFT);
        summaryBar.setStyle(
            "-fx-background-color: #1b2a4a;" +
            "-fx-background-radius: 10;");

        Label summaryIcon = new Label("📅");
        summaryIcon.setFont(Font.font(20));

        Label summaryText = new Label(
            "You have " +
            upcomingEvents.size() +
            " upcoming event(s). " +
            "Total of " +
            allEvents.size() +
            " event(s) managed so far.");
        summaryText.setFont(Font.font(
            "Arial", 14));
        summaryText.setTextFill(
            Color.web("#cbd5e0"));
        summaryText.setWrapText(true);

        Region sumSpacer = new Region();
        HBox.setHgrow(sumSpacer, Priority.ALWAYS);

        Button createQuickBtn = new Button(
            "+ Create New Event");
        createQuickBtn.setStyle(
            "-fx-background-color: #4361ee;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 20;");
        createQuickBtn.setOnAction(e -> {
            setActive(createBtn, dashBtn,
                myEventsBtn, attendeesBtn);
            contentArea.getChildren().setAll(
                CreateEventView.build(stage,
                    username, organizerId));
        });

        summaryBar.getChildren().addAll(
            summaryIcon, summaryText,
            sumSpacer, createQuickBtn);

        content.getChildren().addAll(
            pageTitle, welcomeCard,
            statsRow, summaryBar,
            upcomingCard, pastCard);

        scroll.setContent(content);
        return AdminUtils.wrap(scroll);
    }

    private static Button actionBtn(
            String text, String color) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 12 24;");
        return btn;
    }

    public static void setActive(
            Button active, Button... others) {
        active.setStyle(
            "-fx-background-color: #243554;" +
            "-fx-text-fill: white;" +
            "-fx-cursor: hand;" +
            "-fx-background-radius: 0;" +
            "-fx-border-color: #4361ee;" +
            "-fx-border-width: 0 0 0 3;");
        active.setFont(Font.font(
            "Arial", FontWeight.BOLD, 14));
        for (Button b : others) {
            b.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #a0aec0;" +
                "-fx-cursor: hand;" +
                "-fx-background-radius: 0;" +
                "-fx-border-color: transparent;" +
                "-fx-border-width: 0 0 0 3;");
            b.setFont(Font.font(
                "Arial", FontWeight.NORMAL, 14));
        }
    }
}