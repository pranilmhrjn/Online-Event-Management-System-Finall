package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class AttendeeUtils {

    public static Button actionBtn(
            String text, String color) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 12 24;");
        return btn;
    }

    public static void setActive(
            Button active, Button... others) {
        active.setStyle(
            "-fx-background-color: #243554;" +
            "-fx-text-fill: white;" +
            "-fx-cursor: hand;" +
            "-fx-background-radius: 0;" +
            "-fx-border-color: #4361ee;" +
            "-fx-border-width: 0 0 0 3;");
        active.setFont(Font.font(
            "Arial", FontWeight.BOLD, 14));
        for (Button b : others) {
            b.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #a0aec0;" +
                "-fx-cursor: hand;" +
                "-fx-background-radius: 0;" +
                "-fx-border-color: transparent;" +
                "-fx-border-width: 0 0 0 3;");
            b.setFont(Font.font(
                "Arial", FontWeight.NORMAL, 14));
        }
    }

    public static TextField pField(
            String value, String prompt) {
        TextField f = new TextField(value);
        f.setPromptText(prompt);
        f.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #cbd5e0;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 10 14;" +
            "-fx-font-size: 13px;");
        f.setMaxWidth(Double.MAX_VALUE);
        f.setPrefHeight(40);
        return f;
    }

    public static Label pLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font(
            "Arial", FontWeight.BOLD, 13));
        l.setTextFill(Color.web("#2d3748"));
        return l;
    }

    public static String comboStyle() {
        return
            "-fx-background-color: white;" +
            "-fx-border-color: #cbd5e0;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-font-size: 13px;" +
            "-fx-pref-height: 38px;" +
            "-fx-pref-width: 180px;";
    }

    public static Label statusBadge(
            String status) {
        Label badge = new Label(status);
        badge.setStyle(
            "-fx-background-color:" +
            (status.equals("Active")
            ? "#e8f5e9" : "#fce4ec") + ";" +
            "-fx-text-fill:" +
            (status.equals("Active")
            ? "#2e7d32" : "#c62828") + ";" +
            "-fx-font-size: 11px;" +
            "-fx-padding: 3 10;" +
            "-fx-background-radius: 15;");
        return badge;
    }

    public static Label msgLabel() {
        Label l = new Label();
        l.setFont(Font.font("Arial", 13));
        l.setWrapText(true);
        return l;
    }

    public static void setSuccess(
            Label lbl, String msg) {
        lbl.setText(msg);
        lbl.setTextFill(Color.web("#38a169"));
    }

    public static void setError(
            Label lbl, String msg) {
        lbl.setText(msg);
        lbl.setTextFill(Color.web("#e53e3e"));
    }
}