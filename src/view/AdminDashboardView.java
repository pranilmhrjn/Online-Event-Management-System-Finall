package view;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AdminDashboardView {

    public static void show(Stage stage, String username) {

        HBox navbar = buildNavbar(username, "Admin", stage);

        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(240);
        sidebar.setStyle("-fx-background-color: #1b2a4a;");

        VBox sidebarHeader = new VBox(4);
        sidebarHeader.setPadding(new Insets(24, 20, 20, 24));
        sidebarHeader.setStyle("-fx-background-color: #162236;");

        Label portalLabel = new Label("Admin Portal");
        portalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        portalLabel.setTextFill(Color.WHITE);

        Label subLabel = new Label("Event Management");
        subLabel.setFont(Font.font("Arial", 12));
        subLabel.setTextFill(Color.web("#a0aec0"));
        sidebarHeader.getChildren().addAll(portalLabel, subLabel);

        Button dashBtn    = sidebarBtn("  Dashboard",           true);
        Button usersBtn   = sidebarBtn("  User Management",     false);
        Button eventsBtn  = sidebarBtn("  Event Management",    false);
        Button reportsBtn = sidebarBtn("  Reports & Analytics", false);

        VBox sidebarMenu = new VBox(4, dashBtn, usersBtn, eventsBtn, reportsBtn);
        sidebarMenu.setPadding(new Insets(16, 0, 0, 0));
        VBox.setVgrow(sidebarMenu, Priority.ALWAYS);

        Button logoutBtn = new Button("  Logout");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setAlignment(Pos.CENTER_LEFT);
        logoutBtn.setPadding(new Insets(12, 20, 12, 24));
        logoutBtn.setFont(Font.font("Arial", 14));
        logoutBtn.setStyle("-fx-background-color: transparent;-fx-text-fill: #e53e3e;-fx-cursor: hand;-fx-background-radius: 0;");
        logoutBtn.setOnAction(e -> EntryView.show(stage));

        VBox logoutBox = new VBox(logoutBtn);
        logoutBox.setPadding(new Insets(0, 0, 20, 0));
        sidebar.getChildren().addAll(sidebarHeader, sidebarMenu, logoutBox);

        StackPane contentArea = new StackPane();
        HBox.setHgrow(contentArea, Priority.ALWAYS);
        contentArea.getChildren().add(AdminDashboardPage.build());

        dashBtn.setOnAction(e -> { setActive(dashBtn, usersBtn, eventsBtn, reportsBtn); contentArea.getChildren().setAll(AdminDashboardPage.build()); });
        usersBtn.setOnAction(e -> { setActive(usersBtn, dashBtn, eventsBtn, reportsBtn); contentArea.getChildren().setAll(AdminUsersPage.build()); });
        eventsBtn.setOnAction(e -> { setActive(eventsBtn, dashBtn, usersBtn, reportsBtn); contentArea.getChildren().setAll(AdminEventsPage.build()); });
        reportsBtn.setOnAction(e -> { setActive(reportsBtn, dashBtn, usersBtn, eventsBtn); contentArea.getChildren().setAll(AdminReportsPage.build()); });

        HBox body = new HBox(sidebar, contentArea);
        VBox.setVgrow(body, Priority.ALWAYS);
        VBox root = new VBox(navbar, body);
        setScene(stage, root, AdminDashboardView.class, "Admin Dashboard - EventPro");
    }

    public static HBox buildNavbar(String username, String roleLabel, Stage stage) {
        HBox navbar = new HBox();
        navbar.setPadding(new Insets(0, 30, 0, 30));
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setPrefHeight(65);
        navbar.setStyle("-fx-background-color: #1b2a4a;");

        Label appName = new Label("EventPro");
        appName.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        appName.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label("  " + username + "  |  " + roleLabel);
        userLabel.setFont(Font.font("Arial", 14));
        userLabel.setTextFill(Color.web("#a0aec0"));

        Button logout = new Button("Logout");
        logout.setStyle("-fx-background-color: #e53e3e;-fx-text-fill: white;-fx-font-size: 13px;-fx-font-weight: bold;-fx-background-radius: 6;-fx-cursor: hand;-fx-padding: 8 18;");
        logout.setOnAction(e -> EntryView.show(stage));

        navbar.getChildren().addAll(appName, spacer, userLabel, new Label("   "), logout);
        return navbar;
    }

    public static VBox buildSidebar(Stage stage, String username, String role, String active) {
        VBox sidebar = new VBox(8);
        sidebar.setPrefWidth(230);
        sidebar.setPadding(new Insets(30, 0, 30, 0));
        sidebar.setStyle("-fx-background-color: #243554;");

        String panelName = switch (role) {
            case "admin"     -> "ADMIN PANEL";
            case "organizer" -> "ORGANIZER PANEL";
            default          -> "ATTENDEE PANEL";
        };

        Label menuTitle = new Label(panelName);
        menuTitle.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        menuTitle.setTextFill(Color.web("#718096"));
        menuTitle.setPadding(new Insets(0, 0, 10, 24));

        Button dashBtn   = sidebarBtn("  Dashboard",     active.equals("dashboard"));
        Button eventsBtn = sidebarBtn("  Manage Events", active.equals("events"));

        dashBtn.setOnAction(e -> {
            switch (role) {
                case "admin"     -> AdminDashboardView.show(stage, username);
                case "organizer" -> OrganizerDashboardView.show(stage, username);
                default          -> AttendeeDashboardView.show(stage, username);
            }
        });
        eventsBtn.setOnAction(e -> EventView.show(stage, username, role));
        sidebar.getChildren().addAll(menuTitle, dashBtn, eventsBtn);
        return sidebar;
    }

    public static Button sidebarBtn(String text, boolean active) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(12, 20, 12, 24));
        btn.setFont(Font.font("Arial", active ? FontWeight.BOLD : FontWeight.NORMAL, 14));
        btn.setStyle(
            "-fx-background-color: " + (active ? "#243554" : "transparent") + ";" +
            "-fx-text-fill: " + (active ? "white" : "#a0aec0") + ";" +
            "-fx-cursor: hand;-fx-background-radius: 0;" +
            "-fx-border-color: " + (active ? "#4361ee" : "transparent") + ";" +
            "-fx-border-width: 0 0 0 3;");
        return btn;
    }

    public static void setActive(Button active, Button... others) {
        active.setStyle("-fx-background-color: #243554;-fx-text-fill: white;-fx-cursor: hand;-fx-background-radius: 0;-fx-border-color: #4361ee;-fx-border-width: 0 0 0 3;");
        active.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        for (Button b : others) {
            b.setStyle("-fx-background-color: transparent;-fx-text-fill: #a0aec0;-fx-cursor: hand;-fx-background-radius: 0;-fx-border-color: transparent;-fx-border-width: 0 0 0 3;");
            b.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        }
    }

    public static VBox welcomeCard(String title, String sub) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: #1b2a4a;-fx-background-radius: 12;");
        Label t = new Label(title);
        t.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        t.setTextFill(Color.WHITE);
        Label s = new Label(sub);
        s.setFont(Font.font("Arial", 15));
        s.setTextFill(Color.web("#a0aec0"));
        card.getChildren().addAll(t, s);
        return card;
    }

    public static VBox statCard(String icon, String title, String sub) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(24));
        card.setPrefWidth(220);
        card.setStyle(cardStyle());
        Label i = new Label(icon); i.setFont(Font.font(28));
        Label t = new Label(title); t.setFont(Font.font("Arial", FontWeight.BOLD, 15)); t.setTextFill(Color.web("#1b2a4a"));
        Label s = new Label(sub); s.setFont(Font.font("Arial", 12)); s.setTextFill(Color.web("#718096"));
        card.getChildren().addAll(i, t, s);
        return card;
    }

    public static void setScene(Stage stage, VBox root, Class<?> clazz, String title) {
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screen.getWidth(), screen.getHeight());
        scene.getStylesheets().add(clazz.getResource("/application/application.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle(title);
        stage.show();
    }

    public static String cardStyle() { return AdminUtils.cardStyle(); }
    public static String tooltipStyle() { return AdminUtils.tooltipStyle(); }
    public static VBox bigStatCard(String title, String value, String color, String sub) { return AdminUtils.statCard(title, value, color, sub); }
}
