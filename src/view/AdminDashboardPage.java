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

public class AdminDashboardPage {

    public static VBox build() {

        ScrollPane scroll = AdminUtils.scroll();
        VBox content = AdminUtils.content();
        Label pageTitle = AdminUtils.pageTitle("Dashboard");

        UserModel um = new UserModel();
        EventController ec = new EventController();

        HBox statsRow = new HBox(20);
        statsRow.getChildren().addAll(
            AdminUtils.statCard("Total Users", String.valueOf(um.getAllUsers().size()), "#4361ee", "Registered accounts"),
            AdminUtils.statCard("Total Events", String.valueOf(ec.getAllEvents().size()), "#38a169", "Active events"),
            AdminUtils.statCard("Registrations", String.valueOf(dbCount("SELECT COUNT(*) FROM registrations")), "#e67e22", "Event registrations"),
            AdminUtils.statCard("System Status", "Online", "#2dc653", "All services running"));

        VBox barCard = AdminUtils.card();
        Label barTitle = AdminUtils.cardTitle("Monthly Events Overview");

        String[] months = {"Jan","Feb","Mar","Apr","May","Jun"};
        int[]    values = monthlyEvents();
        int      maxVal = AdminUtils.maxVal(values);

        Canvas barCanvas = new Canvas(700, 220);
        AdminCharts.drawBarChart(barCanvas.getGraphicsContext2D(), 700, 220, values, maxVal);

        Label barTooltip = AdminUtils.tooltip();
        double barW = 50, baseY = 180;

        barCanvas.setOnMouseMoved(e -> {
            double gap = (700 - 60 - months.length * barW) / (months.length + 1);
            boolean found = false;
            for (int i = 0; i < months.length; i++) {
                double x  = 50 + gap + i * (barW + gap);
                double bH = maxVal == 0 ? 0 : (values[i] / (double) maxVal) * (baseY - 20);
                double bY = baseY - bH;
                if (e.getX() >= x && e.getX() <= x + barW && e.getY() >= bY && e.getY() <= baseY) {
                    barTooltip.setText(months[i] + ": " + values[i] + " events");
                    barTooltip.setVisible(true);
                    found = true;
                    break;
                }
            }
            if (!found) barTooltip.setVisible(false);
        });
        barCanvas.setOnMouseExited(e -> barTooltip.setVisible(false));
        barCard.getChildren().addAll(barTitle, barCanvas, barTooltip);

        VBox recentCard = AdminUtils.card();
        Label recentTitle = AdminUtils.cardTitle("Recent Events");

        TableView<ObservableList<String>> table = new TableView<>();
        table.setPrefHeight(250);
        table.setPlaceholder(new Label("No events added yet"));

        String[] cols = {"Title","Date","Location"}; int[] widths = {300, 180, 220};
        for (int i = 0; i < cols.length; i++) {
            final int idx = i + 1;
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(cols[i]);
            col.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(idx)));
            col.setPrefWidth(widths[i]);
            table.getColumns().add(col);
        }

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        for (String[] row : ec.getAllEvents())
            data.add(FXCollections.observableArrayList(row));
        table.setItems(data);
        recentCard.getChildren().addAll(recentTitle, table);

        content.getChildren().addAll(pageTitle, statsRow, barCard, recentCard);
        scroll.setContent(content);
        return AdminUtils.wrap(scroll);
    }

    private static int[] monthlyEvents() {
        int[] counts = new int[6];
        try {
            Connection conn = DatabaseConnection.getConnection();
            int year = java.time.LocalDate.now().getYear();
            for (int i = 0; i < 6; i++) {
                PreparedStatement pst = conn.prepareStatement(
                    "SELECT COUNT(*) FROM events WHERE MONTH(STR_TO_DATE(date,'%Y-%m-%d'))=? AND YEAR(STR_TO_DATE(date,'%Y-%m-%d'))=?");
                pst.setInt(1, i + 1); pst.setInt(2, year);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) counts[i] = rs.getInt(1);
            }
        } catch (Exception e) { System.out.println("Bar: " + e.getMessage()); }
        return counts;
    }

    private static int dbCount(String sql) {
        try {
            ResultSet rs = DatabaseConnection.getConnection().prepareStatement(sql).executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {}
        return 0;
    }
}
