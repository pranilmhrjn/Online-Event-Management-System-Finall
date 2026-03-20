package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class AdminUtils {

    public static ScrollPane scroll() {
        ScrollPane s = new ScrollPane();
        s.setFitToWidth(true);
        s.setStyle("-fx-background-color: #f0f2f7;-fx-background: #f0f2f7;");
        return s;
    }

    public static VBox content() {
        VBox v = new VBox(24);
        v.setPadding(new Insets(36));
        v.setStyle("-fx-background-color: #f0f2f7;");
        return v;
    }

    public static VBox wrap(ScrollPane scroll) {
        VBox w = new VBox(scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        VBox.setVgrow(w, Priority.ALWAYS);
        return w;
    }

    public static VBox card() {
        VBox c = new VBox(14);
        c.setPadding(new Insets(24));
        c.setStyle(cardStyle());
        return c;
    }

    public static Label pageTitle(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        l.setTextFill(Color.web("#1b2a4a"));
        return l;
    }

    public static Label cardTitle(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        l.setTextFill(Color.web("#1b2a4a"));
        return l;
    }

    public static Label tooltip() {
        Label l = new Label("");
        l.setStyle("-fx-background-color: #1b2a4a;-fx-text-fill: white;-fx-padding: 4 10;-fx-background-radius: 5;-fx-font-size: 12px;");
        l.setVisible(false);
        return l;
    }

    public static VBox statCard(String title, String value, String color, String sub) {
        VBox c = new VBox(6);
        c.setPadding(new Insets(22));
        c.setStyle(cardStyle());
        HBox.setHgrow(c, Priority.ALWAYS);
        Label t = new Label(title);
        t.setFont(Font.font("Arial", 13));
        t.setTextFill(Color.web("#718096"));
        Label v = new Label(value);
        v.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        v.setTextFill(Color.web(color));
        Label s = new Label(sub);
        s.setFont(Font.font("Arial", 12));
        s.setTextFill(Color.web("#a0aec0"));
        c.getChildren().addAll(t, v, s);
        return c;
    }

    public static TextField field(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setStyle(LoginView.inputStyle());
        f.setMaxWidth(Double.MAX_VALUE);
        f.setPrefHeight(38);
        return f;
    }

    public static Label lbl(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        l.setTextFill(Color.web("#2d3748"));
        return l;
    }

    public static int maxVal(int[] values) {
        int max = 1;
        for (int v : values) if (v > max) max = v;
        return max + 2;
    }

    public static String cardStyle() {
        return "-fx-background-color: white;-fx-background-radius: 10;-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.07),10,0,0,3);";
    }

    public static String tooltipStyle() {
        return "-fx-background-color: #1b2a4a;-fx-text-fill: white;-fx-padding: 4 10;-fx-background-radius: 5;-fx-font-size: 12px;";
    }
}
