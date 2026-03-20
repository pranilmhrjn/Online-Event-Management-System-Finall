package view;

import controller.LoginController;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LoginView {

    public static void show(Stage stage, String role) {

        LoginController controller = new LoginController();

        VBox leftPanel = new VBox(24);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setPrefWidth(500);
        leftPanel.setStyle("-fx-background-color: #1b2a4a;");
        leftPanel.setPadding(new Insets(60));

        Label appName = new Label("EventPro");
        appName.setFont(Font.font("Arial", FontWeight.BOLD, 42));
        appName.setTextFill(Color.WHITE);

        Label roleTag = new Label("Logging in as: " + capitalize(role));
        roleTag.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        roleTag.setTextFill(Color.web("#4361ee"));
        roleTag.setStyle("-fx-background-color: rgba(67,97,238,0.15);-fx-padding: 6 16;-fx-background-radius: 20;");

        Label desc = new Label("Welcome back!\nSign in to access your\n" + capitalize(role) + " dashboard.");
        desc.setFont(Font.font("Arial", 14));
        desc.setTextFill(Color.web("#cbd5e0"));
        desc.setTextAlignment(TextAlignment.CENTER);
        desc.setWrapText(true);

        leftPanel.getChildren().addAll(appName, roleTag, desc);

        VBox rightPanel = new VBox(14);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setStyle("-fx-background-color: #f7f8fc;");
        rightPanel.setPadding(new Insets(60, 80, 60, 80));
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        Label title = new Label("Welcome Back");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#1b2a4a"));

        Label subtitle = new Label("Sign in to your " + capitalize(role) + " account");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#718096"));

        Label userLabel = new Label("Username");
        userLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        userLabel.setTextFill(Color.web("#2d3748"));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setStyle(inputStyle());
        usernameField.setMaxWidth(400);
        usernameField.setPrefHeight(44);

        Label passLabel = new Label("Password");
        passLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        passLabel.setTextFill(Color.web("#2d3748"));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle(inputStyle());
        passwordField.setMaxWidth(400);
        passwordField.setPrefHeight(44);

        Button loginBtn = new Button("Sign In as " + capitalize(role));
        loginBtn.setStyle(primaryBtnStyle());
        loginBtn.setMaxWidth(400);
        loginBtn.setPrefHeight(46);

        Button backBtn = new Button("← Change Role");
        backBtn.setStyle(linkBtnStyle());

        Button signupLink = new Button("Don't have an account? Sign Up");
        signupLink.setStyle("-fx-background-color: transparent;-fx-text-fill: #718096;-fx-cursor: hand;-fx-font-size: 13px;");

        Label message = new Label();
        message.setFont(Font.font("Arial", 13));
        message.setWrapText(true);

        loginBtn.setOnAction(e -> {
            String u = usernameField.getText().trim();
            String p = passwordField.getText().trim();
            if (u.isEmpty() || p.isEmpty()) {
                message.setText("⚠  Please fill in all fields.");
                message.setTextFill(Color.web("#e53e3e"));
                return;
            }
            String result = controller.handleLogin(u, p, role);
            if (result != null) {
                message.setText("✔  Login successful!");
                message.setTextFill(Color.web("#38a169"));
                if (role.equals("admin")) {
                    AdminDashboardView.show(stage, u);
                } else if (role.equals("organizer")) {
                    OrganizerDashboardView.show(stage, u);
                } else if (role.equals("attendee")) {
                    AttendeeDashboardView.show(stage, u);
                }
            } else {
                message.setText("✘  Invalid credentials for " + role + ".");
                message.setTextFill(Color.web("#e53e3e"));
            }
        });

        backBtn.setOnAction(e -> RoleSelectionView.show(stage, "login"));
        signupLink.setOnAction(e -> RoleSelectionView.show(stage, "signup"));

        VBox formBox = new VBox(10, title, subtitle, new Region(),
            userLabel, usernameField, passLabel, passwordField,
            loginBtn, backBtn, signupLink, message);
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setMaxWidth(400);

        rightPanel.getChildren().add(formBox);

        HBox root = new HBox(leftPanel, rightPanel);
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screen.getWidth(), screen.getHeight());
        scene.getStylesheets().add(
            LoginView.class.getResource("/application/application.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Login - EventPro");
        stage.show();
    }

    public static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String inputStyle() {
        return "-fx-background-color: white;-fx-border-color: #cbd5e0;-fx-border-radius: 8;-fx-background-radius: 8;-fx-padding: 10 14;-fx-font-size: 14px;";
    }

    public static String primaryBtnStyle() {
        return "-fx-background-color: #1b2a4a;-fx-text-fill: white;-fx-font-size: 14px;-fx-font-weight: bold;-fx-background-radius: 8;-fx-cursor: hand;";
    }

    public static String linkBtnStyle() {
        return "-fx-background-color: transparent;-fx-text-fill: #4361ee;-fx-cursor: hand;-fx-font-size: 13px;";
    }
}
