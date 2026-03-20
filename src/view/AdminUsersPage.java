package view;

import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import model.DatabaseConnection;
import java.sql.*;

public class AdminUsersPage {

    public static VBox build() {

        ScrollPane scroll = AdminUtils.scroll();
        VBox content = AdminUtils.content();
        Label pageTitle = AdminUtils.pageTitle("User Management");

        VBox tableCard = AdminUtils.card();

        TextField searchField = new TextField();
        searchField.setPromptText("Search users...");
        searchField.setStyle(LoginView.inputStyle());
        searchField.setPrefWidth(260); searchField.setPrefHeight(38);

        ComboBox<String> roleFilter = new ComboBox<>();
        roleFilter.getItems().addAll("All Roles","admin","organizer","attendee");
        roleFilter.setValue("All Roles");
        roleFilter.setStyle("-fx-background-color: white;-fx-border-color: #cbd5e0;-fx-border-radius: 6;-fx-background-radius: 6;-fx-font-size: 13px;-fx-pref-height: 38px;-fx-pref-width: 160px;");

        HBox searchRow = new HBox(12, searchField, roleFilter);
        searchRow.setAlignment(Pos.CENTER_LEFT);

        TableView<ObservableList<String>> table = new TableView<>();
        table.setPrefHeight(450);
        table.setPlaceholder(new Label("No users found"));

        TableColumn<ObservableList<String>, String> snCol = new TableColumn<>("#");
        snCol.setPrefWidth(50);
        snCol.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : String.valueOf(getIndex() + 1)); }
        });
        table.getColumns().add(snCol);

        String[] cols = {"Username","Email","Role"}; int[] widths = {200, 260, 120};
        for (int i = 0; i < cols.length; i++) {
            final int idx = i + 1;
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(cols[i]);
            col.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().get(idx)));
            col.setPrefWidth(widths[i]);
            table.getColumns().add(col);
        }

        Label statusMsg = new Label(); statusMsg.setFont(Font.font("Arial", 13));

        TableColumn<ObservableList<String>, Void> editCol = new TableColumn<>("Edit Role");
        editCol.setPrefWidth(100);
        editCol.setCellFactory(col -> new TableCell<>() {
            Button btn = new Button("Edit");
            { btn.setStyle("-fx-background-color: #4361ee;-fx-text-fill: white;-fx-font-size: 12px;-fx-background-radius: 5;-fx-cursor: hand;-fx-padding: 5 12;");
              btn.setOnAction(e -> {
                ObservableList<String> row = getTableView().getItems().get(getIndex());
                if (row.get(3).equals("admin")) { statusMsg.setText("Cannot edit admin role."); statusMsg.setTextFill(Color.web("#e53e3e")); return; }
                showEditRoleDialog(row, table, statusMsg);
              }); }
            @Override protected void updateItem(Void item, boolean empty) { super.updateItem(item, empty); if (empty) { setGraphic(null); } else { String role = getTableView().getItems().get(getIndex()).get(3); setGraphic(role.equals("admin") ? null : btn); } }
        });
        table.getColumns().add(editCol);

        TableColumn<ObservableList<String>, Void> actCol = new TableColumn<>("Delete");
        actCol.setPrefWidth(100);
        actCol.setCellFactory(col -> new TableCell<>() {
            Button del = new Button("Delete");
            { del.setStyle("-fx-background-color: #e53e3e;-fx-text-fill: white;-fx-font-size: 12px;-fx-background-radius: 5;-fx-cursor: hand;-fx-padding: 5 12;");
              del.setOnAction(e -> {
                ObservableList<String> row = getTableView().getItems().get(getIndex());
                String uname = row.get(1), role = row.get(3);
                if (role.equals("admin")) { statusMsg.setText("Cannot delete admin."); statusMsg.setTextFill(Color.web("#e53e3e")); return; }
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete User"); alert.setHeaderText("Delete: " + uname + "?"); alert.setContentText("This cannot be undone.");
                ButtonType yes = new ButtonType("Yes, Delete", ButtonBar.ButtonData.OK_DONE);
                ButtonType no  = new ButtonType("No, Cancel",  ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(yes, no);
                alert.showAndWait().ifPresent(res -> { if (res == yes) { if (deleteUser(Integer.parseInt(row.get(0)))) { statusMsg.setText("User deleted."); statusMsg.setTextFill(Color.web("#38a169")); loadTable(table, roleFilter.getValue(), searchField.getText()); } else { statusMsg.setText("Failed."); statusMsg.setTextFill(Color.web("#e53e3e")); } } });
              }); }
            @Override protected void updateItem(Void item, boolean empty) { super.updateItem(item, empty); if (empty) { setGraphic(null); } else { String role = getTableView().getItems().get(getIndex()).get(3); setGraphic(role.equals("admin") ? null : del); } }
        });
        table.getColumns().add(actCol);

        loadTable(table, "All Roles", "");
        searchField.textProperty().addListener((o, v, n) -> loadTable(table, roleFilter.getValue(), n));
        roleFilter.setOnAction(e -> loadTable(table, roleFilter.getValue(), searchField.getText()));

        tableCard.getChildren().addAll(searchRow, table, statusMsg);
        content.getChildren().addAll(pageTitle, tableCard);
        scroll.setContent(content);
        return AdminUtils.wrap(scroll);
    }

    private static void showEditRoleDialog(ObservableList<String> row,
            TableView<ObservableList<String>> table, Label statusMsg) {
        Stage dialog = new Stage();
        dialog.setTitle("Edit User Role");

        VBox layout = new VBox(16);
        layout.setPadding(new Insets(28));
        layout.setStyle("-fx-background-color: white;");
        layout.setPrefWidth(360);

        Label title = new Label("Edit User Role"); title.setFont(Font.font("Arial", FontWeight.BOLD, 20)); title.setTextFill(Color.web("#1b2a4a"));
        Label userLbl = new Label("User:  " + row.get(1)); userLbl.setFont(Font.font("Arial", 14)); userLbl.setTextFill(Color.web("#555555"));
        Label currentLbl = new Label("Current Role:  " + row.get(3)); currentLbl.setFont(Font.font("Arial", 13)); currentLbl.setTextFill(Color.web("#718096"));
        Label roleLbl = new Label("Select New Role:"); roleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 13)); roleLbl.setTextFill(Color.web("#2d3748"));

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("organizer", "attendee");
        roleBox.setValue(row.get(3));
        roleBox.setStyle("-fx-background-color: white;-fx-border-color: #cbd5e0;-fx-border-radius: 6;-fx-background-radius: 6;-fx-font-size: 13px;-fx-pref-height: 38px;-fx-pref-width: 300px;");

        Button saveBtn = new Button("Update Role"); saveBtn.setStyle(LoginView.primaryBtnStyle()); saveBtn.setPrefHeight(40); saveBtn.setPrefWidth(300);
        Button cancelBtn = new Button("Cancel"); cancelBtn.setStyle("-fx-background-color: #e2e8f0;-fx-text-fill: #1b2a4a;-fx-font-size: 13px;-fx-background-radius: 8;-fx-cursor: hand;-fx-padding: 10 24;");
        cancelBtn.setOnAction(e -> dialog.close());

        Label msg = new Label(); msg.setFont(Font.font("Arial", 13));

        saveBtn.setOnAction(e -> {
            if (updateUserRole(Integer.parseInt(row.get(0)), roleBox.getValue())) {
                statusMsg.setText("Role updated for " + row.get(1)); statusMsg.setTextFill(Color.web("#38a169"));
                loadTable(table, "All Roles", ""); dialog.close();
            } else { msg.setText("Update failed."); msg.setTextFill(Color.web("#e53e3e")); }
        });

        layout.getChildren().addAll(title, userLbl, currentLbl, roleLbl, roleBox, new HBox(12, saveBtn, cancelBtn), msg);
        dialog.setScene(new Scene(layout, 380, 320));
        dialog.show();
    }

    static void loadTable(TableView<ObservableList<String>> table, String roleFilter, String search) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String s = "%" + (search == null ? "" : search) + "%";
            String sql = roleFilter.equals("All Roles")
                ? "SELECT id,username,email,role FROM users WHERE username LIKE ? OR email LIKE ?"
                : "SELECT id,username,email,role FROM users WHERE role=? AND (username LIKE ? OR email LIKE ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            if (roleFilter.equals("All Roles")) { pst.setString(1, s); pst.setString(2, s); }
            else { pst.setString(1, roleFilter); pst.setString(2, s); pst.setString(3, s); }
            ResultSet rs = pst.executeQuery();
            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            while (rs.next())
                data.add(FXCollections.observableArrayList(String.valueOf(rs.getInt("id")), rs.getString("username"), rs.getString("email"), rs.getString("role")));
            table.setItems(data);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static boolean updateUserRole(int userId, String newRole) {
        try { PreparedStatement pst = DatabaseConnection.getConnection().prepareStatement("UPDATE users SET role=? WHERE id=?"); pst.setString(1, newRole); pst.setInt(2, userId); pst.executeUpdate(); return true; }
        catch (Exception e) { return false; }
    }

    private static boolean deleteUser(int id) {
        try { PreparedStatement pst = DatabaseConnection.getConnection().prepareStatement("DELETE FROM users WHERE id=?"); pst.setInt(1, id); pst.executeUpdate(); return true; }
        catch (Exception e) { return false; }
    }
}
