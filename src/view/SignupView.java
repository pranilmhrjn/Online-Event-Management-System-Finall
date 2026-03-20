package view;

import controller.SignupController;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SignupView {

    public static void show(Stage stage, String role) {

        SignupController controller = new SignupController();

        VBox leftPanel = new VBox(24);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setPrefWidth(500);
        leftPanel.setStyle("-fx-background-color: #1b2a4a;");
        leftPanel.setPadding(new Insets(60));

        Label appName = new Label("EventPro");
        appName.setFont(Font.font("Arial", FontWeight.BOLD, 42));
        appName.setTextFill(Color.WHITE);

        Label roleTag = new Label("Signing up as: " + LoginView.capitalize(role));
        roleTag.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        roleTag.setTextFill(Color.web("#4361ee"));
        roleTag.setStyle("-fx-background-color: rgba(67,97,238,0.15);-fx-padding: 6 16;-fx-background-radius: 20;");

        Label desc = new Label("Create your account\nand join EventPro today.");
        desc.setFont(Font.font("Arial", 14));
        desc.setTextFill(Color.web("#cbd5e0"));
        desc.setTextAlignment(TextAlignment.CENTER);
        leftPanel.getChildren().addAll(appName, roleTag, desc);

        VBox rightPanel = new VBox(14);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setStyle("-fx-background-color: #f7f8fc;");
        rightPanel.setPadding(new Insets(60, 80, 60, 80));
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        if (role.equals("admin") || role.equals("organizer")) {
            Label blockedIcon = new Label("🔒");
            blockedIcon.setFont(Font.font(48));
            Label blockedTitle = new Label("Account Required");
            blockedTitle.setFont(Font.font("Arial", FontWeight.BOLD, 26));
            blockedTitle.setTextFill(Color.web("#1b2a4a"));
            Label blockedMsg = new Label(
                role.equals("admin")
                ? "Admin accounts are pre-configured.\nPlease contact your system administrator\nto get your login credentials."
                : "Organizer accounts are pre-configured.\nPlease contact your system administrator\nto get your login credentials.");
            blockedMsg.setFont(Font.font("Arial", 14));
            blockedMsg.setTextFill(Color.web("#718096"));
            blockedMsg.setTextAlignment(TextAlignment.CENTER);
            blockedMsg.setWrapText(true);
            Button backBtn = new Button("← Go Back");
            backBtn.setStyle(LoginView.primaryBtnStyle());
            backBtn.setPrefHeight(44); backBtn.setPrefWidth(200);
            backBtn.setOnAction(e -> RoleSelectionView.show(stage, "signup"));
            Button loginInstead = new Button("Already have an account? Login");
            loginInstead.setStyle(LoginView.linkBtnStyle());
            loginInstead.setOnAction(e -> LoginView.show(stage, role));
            VBox blockedBox = new VBox(16, blockedIcon, blockedTitle, blockedMsg, backBtn, loginInstead);
            blockedBox.setAlignment(Pos.CENTER);
            blockedBox.setMaxWidth(400);
            rightPanel.getChildren().add(blockedBox);
        } else {
            Label title = new Label("Create Account");
            title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
            title.setTextFill(Color.web("#1b2a4a"));
            Label subtitle = new Label("Register as " + LoginView.capitalize(role));
            subtitle.setFont(Font.font("Arial", 14));
            subtitle.setTextFill(Color.web("#718096"));

            TextField usernameField = styledField("Username");
            TextField emailField    = styledField("Email Address");
            PasswordField passField = new PasswordField();
            passField.setPromptText("Password");
            passField.setStyle(LoginView.inputStyle());
            passField.setMaxWidth(400); passField.setPrefHeight(44);
            PasswordField confirmField = new PasswordField();
            confirmField.setPromptText("Confirm Password");
            confirmField.setStyle(LoginView.inputStyle());
            confirmField.setMaxWidth(400); confirmField.setPrefHeight(44);

            Button signupBtn = new Button("Create Account");
            signupBtn.setStyle(LoginView.primaryBtnStyle());
            signupBtn.setMaxWidth(400); signupBtn.setPrefHeight(46);
            Button backBtn = new Button("← Change Role");
            backBtn.setStyle(LoginView.linkBtnStyle());
            Button loginLink = new Button("Already have an account? Sign In");
            loginLink.setStyle("-fx-background-color: transparent;-fx-text-fill: #718096;-fx-cursor: hand;-fx-font-size: 13px;");

            Label message = new Label();
            message.setFont(Font.font("Arial", 13));

            signupBtn.setOnAction(e -> {
                String result = controller.handleSignup(
                    usernameField.getText().trim(),
                    emailField.getText().trim(),
                    passField.getText().trim(),
                    confirmField.getText().trim(), role);
                switch (result) {
                    case "empty"    -> { message.setText("⚠  Fill in all fields."); message.setTextFill(Color.web("#e53e3e")); }
                    case "mismatch" -> { message.setText("⚠  Passwords do not match."); message.setTextFill(Color.web("#e53e3e")); }
                    case "exists"   -> { message.setText("✘  Username already exists."); message.setTextFill(Color.web("#e53e3e")); }
                    case "success"  -> { message.setText("✔  Account created! You can now login."); message.setTextFill(Color.web("#38a169")); }
                }
            });

            backBtn.setOnAction(e -> RoleSelectionView.show(stage, "signup"));
            loginLink.setOnAction(e -> RoleSelectionView.show(stage, "login"));

            VBox formBox = new VBox(10, title, subtitle, new Region(),
                usernameField, emailField, passField, confirmField,
                signupBtn, backBtn, loginLink, message);
            formBox.setAlignment(Pos.CENTER_LEFT);
            formBox.setMaxWidth(400);
            rightPanel.getChildren().add(formBox);
        }

        HBox root = new HBox(leftPanel, rightPanel);
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screen.getWidth(), screen.getHeight());
        scene.getStylesheets().add(
            SignupView.class.getResource("/application/application.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Sign Up - EventPro");
        stage.show();
    }

    private static TextField styledField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setStyle(LoginView.inputStyle());
        f.setMaxWidth(400); f.setPrefHeight(44);
        return f;
    }
}
