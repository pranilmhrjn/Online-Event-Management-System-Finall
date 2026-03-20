package view;

import controller.EventController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import model.DatabaseConnection;
import model.UserModel;
import java.sql.*;

public class AdminReportsPage {

    public static VBox build() {

        ScrollPane scroll = AdminUtils.scroll();
        VBox content = AdminUtils.content();
        Label pageTitle = AdminUtils.pageTitle("Reports & Analytics");

        EventController ec = new EventController();
        UserModel um = new UserModel();
        int totalEvents = ec.getAllEvents().size();
        int totalUsers  = um.getAllUsers().size();

        HBox statsRow = new HBox(20);
        statsRow.getChildren().addAll(
            AdminUtils.statCard("Total Attendees", String.valueOf(totalUsers), "#4361ee", "Registered users"),
            AdminUtils.statCard("Events Hosted",   String.valueOf(totalEvents), "#38a169", "Total events"),
            AdminUtils.statCard("Total Revenue",   "$0", "#e67e22", "Estimated revenue"),
            AdminUtils.statCard("Avg Rating",      totalEvents == 0 ? "N/A" : "4.5/5", "#9b59b6", "User satisfaction"));

        VBox lineCard = AdminUtils.card();
        Label lineTitle = AdminUtils.cardTitle("Monthly Registrations Trend");

        int[]    lineValues = monthlyRegs();
        String[] lineMonths = {"Jan","Feb","Mar","Apr","May","Jun"};
        int      lineMax    = AdminUtils.maxVal(lineValues);

        Label lineTooltip = AdminUtils.tooltip();
        Canvas lineCanvas = new Canvas(700, 200);

        if (lineMax <= 1) {
            lineCanvas.getGraphicsContext2D().setFill(Color.web("#718096"));
            lineCanvas.getGraphicsContext2D().setFont(Font.font("Arial", 14));
            lineCanvas.getGraphicsContext2D().fillText("No registration data yet.", 60, 100);
        } else {
            AdminCharts.drawLineChart(lineCanvas.getGraphicsContext2D(), 700, 200, lineValues, lineMax);
            double bY = 170, sX = (700 - 60) / (lineValues.length - 1);
            double[] xs = new double[lineValues.length], ys = new double[lineValues.length];
            for (int i = 0; i < lineValues.length; i++) { xs[i] = 50 + i * sX; ys[i] = bY - (lineValues[i] / (double) lineMax) * (bY - 10); }
            lineCanvas.setOnMouseMoved(e -> {
                boolean found = false;
                for (int i = 0; i < lineValues.length; i++) {
                    double dx = e.getX() - xs[i], dy = e.getY() - ys[i];
                    if (Math.sqrt(dx*dx + dy*dy) <= 15) {
                        String ch = i > 0 ? (lineValues[i] - lineValues[i-1] >= 0 ? " (+" + (lineValues[i]-lineValues[i-1]) + " from prev)" : " (" + (lineValues[i]-lineValues[i-1]) + " from prev)") : "";
                        lineTooltip.setText(lineMonths[i] + ": " + lineValues[i] + " registrations" + ch);
                        lineTooltip.setVisible(true); found = true; break;
                    }
                }
                if (!found) lineTooltip.setVisible(false);
            });
            lineCanvas.setOnMouseExited(e -> lineTooltip.setVisible(false));
        }
        lineCard.getChildren().addAll(lineTitle, lineCanvas, lineTooltip);

        VBox attCard = AdminUtils.card();
        Label attTitle = AdminUtils.cardTitle("Event Attendance Overview");

        TableView<ObservableList<String>> attTable = new TableView<>();
        attTable.setPrefHeight(280);
        attTable.setPlaceholder(new Label("No events yet"));

        String[] cols = {"Event","Date","Location","Status"}; int[] widths = {220, 120, 180, 100};
        for (int i = 0; i < cols.length; i++) {
            final int idx = i + 1;
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(cols[i]);
            col.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(idx)));
            col.setPrefWidth(widths[i]);
            attTable.getColumns().add(col);
        }

        ObservableList<ObservableList<String>> attData = FXCollections.observableArrayList();
        for (String[] row : ec.getAllEvents()) {
            ObservableList<String> r = FXCollections.observableArrayList(row);
            r.add("Active"); attData.add(r);
        }
        attTable.setItems(attData);
        attCard.getChildren().addAll(attTitle, attTable);

        content.getChildren().addAll(pageTitle, statsRow, lineCard, attCard);
        scroll.setContent(content);
        return AdminUtils.wrap(scroll);
    }

    private static int[] monthlyRegs() {
        int[] counts = new int[6];
        try {
            Connection conn = DatabaseConnection.getConnection();
            int year = java.time.LocalDate.now().getYear();
            for (int i = 0; i < 6; i++) {
                PreparedStatement pst = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE MONTH(created_at)=? AND YEAR(created_at)=?");
                pst.setInt(1, i + 1); pst.setInt(2, year);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) counts[i] = rs.getInt(1);
            }
        } catch (Exception e) { System.out.println("Line: " + e.getMessage()); }
        return counts;
    }
}
