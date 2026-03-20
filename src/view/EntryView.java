package view;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class EntryView {

    public static void show(Stage stage) {

        VBox leftPanel = new VBox(24);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setPrefWidth(500);
        leftPanel.setStyle("-fx-background-color: #1b2a4a;");
        leftPanel.setPadding(new Insets(60));

        Label appName = new Label("EventPro");
        appName.setFont(Font.font("Arial", FontWeight.BOLD, 52));
        appName.setTextFill(Color.WHITE);

        Label tagline = new Label("Online Event Management System");
        tagline.setFont(Font.font("Arial", 16));
        tagline.setTextFill(Color.web("#a0aec0"));
        tagline.setTextAlignment(TextAlignment.CENTER);
        tagline.setWrapText(true);

        Separator sep = new Separator();
        sep.setMaxWidth(80);

        Label desc = new Label("The all-in-one platform for\nmanaging events efficiently.\nSimple. Fast. Professional.");
        desc.setFont(Font.font("Arial", 14));
        desc.setTextFill(Color.web("#cbd5e0"));
        desc.setTextAlignment(TextAlignment.CENTER);
        desc.setWrapText(true);

        leftPanel.getChildren().addAll(appName, tagline, sep, desc);

        VBox rightPanel = new VBox(18);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setStyle("-fx-background-color: #f7f8fc;");
        rightPanel.setPadding(new Insets(80));
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        Label title = new Label("Get Started");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Color.web("#1b2a4a"));

        Label subtitle = new Label("Login or create a new account to continue");
        subtitle.setFont(Font.font("Arial", 15));
        subtitle.setTextFill(Color.web("#718096"));

        Button loginBtn = new Button("Login to Your Account");
        loginBtn.setStyle("-fx-background-color: #1b2a4a;-fx-text-fill: white;-fx-font-size: 15px;-fx-font-weight: bold;-fx-background-radius: 8;-fx-cursor: hand;-fx-pref-width: 340px;-fx-pref-height: 50px;");

        Button signupBtn = new Button("Create New Account");
        signupBtn.setStyle("-fx-background-color: white;-fx-text-fill: #1b2a4a;-fx-font-size: 15px;-fx-font-weight: bold;-fx-background-radius: 8;-fx-cursor: hand;-fx-pref-width: 340px;-fx-pref-height: 50px;-fx-border-color: #1b2a4a;-fx-border-radius: 8;");

        loginBtn.setOnAction(e -> RoleSelectionView.show(stage, "login"));
        signupBtn.setOnAction(e -> RoleSelectionView.show(stage, "signup"));

        Region gap = new Region();
        gap.setPrefHeight(20);
        rightPanel.getChildren().addAll(title, subtitle, gap, loginBtn, signupBtn);

        HBox root = new HBox(leftPanel, rightPanel);
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screen.getWidth(), screen.getHeight());
        scene.getStylesheets().add(
            EntryView.class.getResource("/application/application.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("EventPro - Online Event Management System");
        stage.show();
    }
}
