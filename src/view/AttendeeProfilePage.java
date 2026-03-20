package view;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import model.UserModel;

public class AttendeeProfilePage {

    public static VBox build(Stage stage,
            String username, int userId) {

        UserModel um = new UserModel();
        ScrollPane scroll = AdminUtils.scroll();
        VBox content = AdminUtils.content();

        Label pageTitle =
            AdminUtils.pageTitle("My Profile");

        String[] profile =
            um.getUserProfile(username);
        String email =
            profile != null ? profile[2] : "";
        String phone =
            profile != null ? profile[4] : "";
        String bio =
            profile != null ? profile[5] : "";

        VBox profileCard = AdminUtils.card();
        Label profileTitle =
            AdminUtils.cardTitle(
            "Profile Information");

        // Avatar
        VBox avatarBox = new VBox(8);
        avatarBox.setAlignment(Pos.CENTER);
        avatarBox.setPadding(
            new Insets(0, 20, 0, 0));

        Label avatar = new Label(
            username.substring(0, 1)
            .toUpperCase());
        avatar.setMinSize(80, 80);
        avatar.setMaxSize(80, 80);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle(
            "-fx-background-color: #4361ee;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 36px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 40;");

        Label usernameLbl = new Label(username);
        usernameLbl.setFont(Font.font(
            "Arial", FontWeight.BOLD, 16));
        usernameLbl.setTextFill(
            Color.web("#1b2a4a"));

        Label roleBadge = new Label("Attendee");
        roleBadge.setStyle(
            "-fx-background-color: #e3f2fd;" +
            "-fx-text-fill: #1565c0;" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 4 14;" +
            "-fx-background-radius: 20;");

        avatarBox.getChildren().addAll(
            avatar, usernameLbl, roleBadge);

        // Grid
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(14);

        TextField emailField =
            AttendeeUtils.pField(
            email, "Email Address");
        TextField phoneField =
            AttendeeUtils.pField(
            phone, "Phone Number");

        TextArea bioArea = new TextArea(bio);
        bioArea.setPromptText(
            "Tell us about yourself...");
        bioArea.setPrefRowCount(3);
        bioArea.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #cbd5e0;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-font-size: 13px;");
        bioArea.setMaxWidth(Double.MAX_VALUE);

        grid.add(avatarBox,             0, 0, 1, 4);
        grid.add(AttendeeUtils
            .pLabel("Email"),           1, 0);
        grid.add(emailField,            1, 1);
        grid.add(AttendeeUtils
            .pLabel("Phone"),           2, 0);
        grid.add(phoneField,            2, 1);
        grid.add(AttendeeUtils
            .pLabel("Bio"),             1, 2);
        grid.add(bioArea,               1, 3, 2, 1);

        ColumnConstraints c1 =
            new ColumnConstraints();
        c1.setPercentWidth(20);
        ColumnConstraints c2 =
            new ColumnConstraints();
        c2.setPercentWidth(40);
        ColumnConstraints c3 =
            new ColumnConstraints();
        c3.setPercentWidth(40);
        grid.getColumnConstraints()
            .addAll(c1, c2, c3);

        Button saveBtn =
            new Button("💾  Save Changes");
        saveBtn.setStyle(
            "-fx-background-color: #1b2a4a;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 12 28;");

        Label profileMsg =
            AttendeeUtils.msgLabel();

        saveBtn.setOnAction(e -> {
            boolean ok = um.updateProfile(
                username,
                emailField.getText().trim(),
                phoneField.getText().trim(),
                bioArea.getText().trim());
            if (ok) AttendeeUtils.setSuccess(
                profileMsg,
                "✔  Profile updated!");
            else AttendeeUtils.setError(
                profileMsg,
                "✘  Update failed.");
        });

        profileCard.getChildren().addAll(
            profileTitle, grid,
            saveBtn, profileMsg);

        content.getChildren().addAll(
            pageTitle, profileCard);
        scroll.setContent(content);
        return AdminUtils.wrap(scroll);
    }
}