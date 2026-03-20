package view;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class RoleSelectionView {

    public static void show(Stage stage, String mode) {

        VBox leftPanel = new VBox(24);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setPrefWidth(500);
        leftPanel.setStyle("-fx-background-color: #1b2a4a;");
        leftPanel.setPadding(new Insets(60));

        Label appName = new Label("EventPro");
        appName.setFont(Font.font("Arial", FontWeight.BOLD, 42));
        appName.setTextFill(Color.WHITE);

        Label tagline = new Label("Online Event Management System");
        tagline.setFont(Font.font("Arial", 16));
        tagline.setTextFill(Color.web("#a0aec0"));
        tagline.setTextAlignment(TextAlignment.CENTER);
        tagline.setWrapText(true);

        Separator sep = new Separator();
        sep.setMaxWidth(80);

        Label desc = new Label("Select your role to continue.\nEach role has its own\nspecialized dashboard.");
        desc.setFont(Font.font("Arial", 14));
        desc.setTextFill(Color.web("#cbd5e0"));
        desc.setTextAlignment(TextAlignment.CENTER);
        desc.setWrapText(true);

        leftPanel.getChildren().addAll(appName, tagline, sep, desc);

        VBox rightPanel = new VBox(28);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setStyle("-fx-background-color: #f7f8fc;");
        rightPanel.setPadding(new Insets(60, 80, 60, 80));
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        Label title = new Label(mode.equals("login") ? "Login As" : "Sign Up As");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 34));
        title.setTextFill(Color.web("#1b2a4a"));

        Label subtitle = new Label("Choose your role to continue");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#718096"));

        HBox rolesRow = new HBox(24);
        rolesRow.setAlignment(Pos.CENTER);

        VBox adminCard    = roleCard("🛡️", "Admin",     "Manage users,\nevents & system");
        VBox orgCard      = roleCard("🎯", "Organizer", "Create & manage\nyour events");
        VBox attendeeCard = roleCard("🎫", "Attendee",  "Browse & register\nfor events");

        rolesRow.getChildren().addAll(adminCard, orgCard, attendeeCard);

        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-background-color: transparent;-fx-text-fill: #4361ee;-fx-cursor: hand;-fx-font-size: 13px;");
        backBtn.setOnAction(e -> EntryView.show(stage));

        adminCard.setOnMouseClicked(e -> {
            if (mode.equals("login")) LoginView.show(stage, "admin");
            else SignupView.show(stage, "admin");
        });
        orgCard.setOnMouseClicked(e -> {
            if (mode.equals("login")) LoginView.show(stage, "organizer");
            else SignupView.show(stage, "organizer");
        });
        attendeeCard.setOnMouseClicked(e -> {
            if (mode.equals("login")) LoginView.show(stage, "attendee");
            else SignupView.show(stage, "attendee");
        });

        rightPanel.getChildren().addAll(title, subtitle, rolesRow, backBtn);

        HBox root = new HBox(leftPanel, rightPanel);
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screen.getWidth(), screen.getHeight());
        scene.getStylesheets().add(
            RoleSelectionView.class.getResource("/application/application.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Select Role - EventPro");
        stage.show();
    }

    private static VBox roleCard(String icon, String title, String desc) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setPrefWidth(185); card.setPrefHeight(185);

        String normalStyle = "-fx-background-color: white;-fx-background-radius: 12;-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.08),10,0,0,3);-fx-cursor: hand;";
        String hoverStyle  = "-fx-background-color: #1b2a4a;-fx-background-radius: 12;-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.18),15,0,0,5);-fx-cursor: hand;";
        card.setStyle(normalStyle);

        Label iconLabel  = new Label(icon); iconLabel.setFont(Font.font(36));
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.web("#1b2a4a"));
        Label descLabel  = new Label(desc);
        descLabel.setFont(Font.font("Arial", 12));
        descLabel.setTextFill(Color.web("#718096"));
        descLabel.setTextAlignment(TextAlignment.CENTER);
        descLabel.setWrapText(true);

        card.setOnMouseEntered(e -> { card.setStyle(hoverStyle); titleLabel.setTextFill(Color.WHITE); descLabel.setTextFill(Color.web("#a0aec0")); });
        card.setOnMouseExited(e  -> { card.setStyle(normalStyle); titleLabel.setTextFill(Color.web("#1b2a4a")); descLabel.setTextFill(Color.web("#718096")); });

        card.getChildren().addAll(iconLabel, titleLabel, descLabel);
        return card;
    }
}
