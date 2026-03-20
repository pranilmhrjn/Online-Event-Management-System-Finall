package view;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import model.UserModel;

public class AttendeePasswordPage {

    public static VBox build(Stage stage,
            String username, int userId) {

        UserModel um = new UserModel();
        ScrollPane scroll = AdminUtils.scroll();
        VBox content = AdminUtils.content();

        Label pageTitle =
            AdminUtils.pageTitle(
            "Change Password");

        VBox passCard = AdminUtils.card();
        Label passTitle =
            AdminUtils.cardTitle(
            "Update Your Password");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(14);

        PasswordField oldPassField =
            passField("Current password");
        PasswordField newPassField =
            passField("New password");
        PasswordField confirmField =
            passField("Confirm new password");

        grid.add(AttendeeUtils.pLabel(
            "Current Password"), 0, 0);
        grid.add(oldPassField,   0, 1);
        grid.add(AttendeeUtils.pLabel(
            "New Password"),     1, 0);
        grid.add(newPassField,   1, 1);
        grid.add(AttendeeUtils.pLabel(
            "Confirm Password"), 0, 2);
        grid.add(confirmField,   0, 3);

        ColumnConstraints pc1 =
            new ColumnConstraints();
        pc1.setPercentWidth(50);
        ColumnConstraints pc2 =
            new ColumnConstraints();
        pc2.setPercentWidth(50);
        grid.getColumnConstraints()
            .addAll(pc1, pc2);

        // Password rules info box
        VBox rulesBox = new VBox(6);
        rulesBox.setPadding(
            new Insets(16, 20, 16, 20));
        rulesBox.setStyle(
            "-fx-background-color: #f0f4ff;" +
            "-fx-background-radius: 8;");

        Label rulesTitle = new Label(
            "Password Requirements:");
        rulesTitle.setFont(Font.font(
            "Arial", FontWeight.BOLD, 13));
        Label rule1 = new Label(
            "✔  Minimum 6 characters");
        Label rule2 = new Label(
            "✔  Must match confirmation");
        Label rule3 = new Label(
            "✔  Current password required");
        for (Label r : new Label[]{
            rule1, rule2, rule3}) {
            r.setFont(Font.font("Arial", 12));
        }
        rulesBox.getChildren().addAll(
            rulesTitle, rule1, rule2, rule3);

        Button changeBtn =
            new Button("🔒  Change Password");
        changeBtn.setStyle(
            "-fx-background-color: #4361ee;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 12 28;");

        Button clearBtn = new Button("Clear");
        clearBtn.setStyle(
            "-fx-background-color: #e2e8f0;" +
            "-fx-text-fill: #1b2a4a;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 12 20;");
        clearBtn.setOnAction(e -> {
            oldPassField.clear();
            newPassField.clear();
            confirmField.clear();
        });

        HBox btnRow = new HBox(12,
            changeBtn, clearBtn);

        Label passMsg =
            AttendeeUtils.msgLabel();

        changeBtn.setOnAction(e -> {
            String oldP =
                oldPassField.getText().trim();
            String newP =
                newPassField.getText().trim();
            String conP =
                confirmField.getText().trim();

            if (oldP.isEmpty() ||
                newP.isEmpty() ||
                conP.isEmpty()) {
                AttendeeUtils.setError(passMsg,
                    "⚠  Please fill all fields.");
                return;
            }
            if (!newP.equals(conP)) {
                AttendeeUtils.setError(passMsg,
                    "⚠  Passwords do not match.");
                return;
            }
            if (newP.length() < 6) {
                AttendeeUtils.setError(passMsg,
                    "⚠  Min 6 characters.");
                return;
            }
            if (newP.equals(oldP)) {
                AttendeeUtils.setError(passMsg,
                    "⚠  New password must be " +
                    "different.");
                return;
            }

            boolean ok = um.changePassword(
                username, oldP, newP);
            if (ok) {
                AttendeeUtils.setSuccess(passMsg,
                    "✔  Password changed!");
                oldPassField.clear();
                newPassField.clear();
                confirmField.clear();
            } else {
                AttendeeUtils.setError(passMsg,
                    "✘  Current password " +
                    "is incorrect.");
            }
        });

        passCard.getChildren().addAll(
            passTitle, grid, rulesBox,
            btnRow, passMsg);

        content.getChildren().addAll(
            pageTitle, passCard);
        scroll.setContent(content);
        return AdminUtils.wrap(scroll);
    }

    private static PasswordField passField(
            String prompt) {
        PasswordField f = new PasswordField();
        f.setPromptText(prompt);
        f.setStyle(LoginView.inputStyle());
        f.setMaxWidth(Double.MAX_VALUE);
        f.setPrefHeight(40);
        return f;
    }
}