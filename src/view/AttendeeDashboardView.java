package view;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import model.UserModel;

public class AttendeeDashboardView {

    public static void show(
            Stage stage, String username) {

        int userId =
            new UserModel().getUserId(username);

        HBox navbar = AdminDashboardView.buildNavbar(username,"Attendee", stage);

        VBox sidebar = buildSidebar(stage,
            username, userId, null);

        StackPane contentArea = new StackPane();
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        // Get sidebar buttons for setActive
        Button dashBtn = AdminDashboardView.sidebarBtn("  Dashboard", true);
        Button browseBtn = AdminDashboardView.sidebarBtn("  Browse Events", false);
       
        Button myRegBtn = AdminDashboardView
            .sidebarBtn(
            "  My Registrations", false);
        Button profileBtn = AdminDashboardView
            .sidebarBtn("  My Profile", false);
        Button passBtn = AdminDashboardView
            .sidebarBtn(
            "  Change Password", false);

        VBox sideMenu = new VBox(4, dashBtn,
            browseBtn, myRegBtn,
            profileBtn, passBtn);
        sideMenu.setPadding(
            new Insets(16, 0, 0, 0));
        VBox.setVgrow(sideMenu, Priority.ALWAYS);

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

        VBox sidebarHeader = new VBox(4);
        sidebarHeader.setPadding(
            new Insets(24, 20, 20, 24));
        sidebarHeader.setStyle(
            "-fx-background-color: #162236;");
        Label portalLabel =
            new Label("Attendee Portal");
        portalLabel.setFont(Font.font(
            "Arial", FontWeight.BOLD, 16));
        portalLabel.setTextFill(Color.WHITE);
        Label subLabel =
            new Label("Event Discovery");
        subLabel.setFont(Font.font("Arial", 12));
        subLabel.setTextFill(Color.web("#a0aec0"));
        sidebarHeader.getChildren().addAll(
            portalLabel, subLabel);

        VBox sidebarBox = new VBox(0);
        sidebarBox.setPrefWidth(240);
        sidebarBox.setStyle(
            "-fx-background-color: #1b2a4a;");
        sidebarBox.getChildren().addAll(
            sidebarHeader, sideMenu, logoutBox);

        contentArea.getChildren().add(
            AttendeeDashboardPage.build(stage,
                username, userId, contentArea,
                dashBtn, browseBtn, myRegBtn,
                profileBtn, passBtn));

        dashBtn.setOnAction(e -> {
            AttendeeUtils.setActive(dashBtn,
                browseBtn, myRegBtn,
                profileBtn, passBtn);
            contentArea.getChildren().setAll(
                AttendeeDashboardPage.build(stage,
                    username, userId, contentArea,
                    dashBtn, browseBtn, myRegBtn,
                    profileBtn, passBtn));
        });
        browseBtn.setOnAction(e -> {
            AttendeeUtils.setActive(browseBtn,
                dashBtn, myRegBtn,
                profileBtn, passBtn);
            contentArea.getChildren().setAll(
                AttendeeBrowsePage.build(stage,
                    username, userId));
        });
        myRegBtn.setOnAction(e -> {
            AttendeeUtils.setActive(myRegBtn,
                dashBtn, browseBtn,
                profileBtn, passBtn);
            contentArea.getChildren().setAll(
                AttendeeMyEventsPage.build(stage,
                    username, userId));
        });
        profileBtn.setOnAction(e -> {
            AttendeeUtils.setActive(profileBtn,
                dashBtn, browseBtn,
                myRegBtn, passBtn);
            contentArea.getChildren().setAll(
                AttendeeProfilePage.build(stage,
                    username, userId));
        });
        passBtn.setOnAction(e -> {
            AttendeeUtils.setActive(passBtn,
                dashBtn, browseBtn,
                myRegBtn, profileBtn);
            contentArea.getChildren().setAll(
                AttendeePasswordPage.build(stage,
                    username, userId));
        });

        HBox body = new HBox(
            sidebarBox, contentArea);
        VBox.setVgrow(body, Priority.ALWAYS);
        VBox root = new VBox(navbar, body);

        AdminDashboardView.setScene(stage, root,
            AttendeeDashboardView.class,
            "Attendee Dashboard - EventPro");
    }

    private static VBox buildSidebar(
            Stage stage, String username,
            int userId, StackPane contentArea) {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(240);
        sidebar.setStyle(
            "-fx-background-color: #1b2a4a;");
        return sidebar;
    }
}