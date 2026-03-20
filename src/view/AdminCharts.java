package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AdminCharts {

    private static final String[] MONTHS =
        {"Jan","Feb","Mar","Apr","May","Jun"};

    public static void drawBarChart(GraphicsContext gc, double w, double h,
                                    int[] values, int maxVal) {
        double barW = 50, baseY = h - 40;
        double gap = (w - 60 - MONTHS.length * barW) / (MONTHS.length + 1);

        gc.setStroke(Color.web("#e2e8f0"));
        gc.setLineWidth(1);
        for (int i = 0; i <= 4; i++) {
            double y = 20 + (baseY - 20) * i / 4;
            gc.strokeLine(50, y, w - 10, y);
            gc.setFill(Color.web("#718096"));
            gc.setFont(Font.font("Arial", 10));
            gc.fillText(String.valueOf((int)(maxVal * (4 - i) / 4)), 8, y + 4);
        }

        for (int i = 0; i < MONTHS.length; i++) {
            double x    = 50 + gap + i * (barW + gap);
            double barH = maxVal == 0 ? 0 : (values[i] / (double) maxVal) * (baseY - 20);
            double barY = baseY - barH;
            if (values[i] == 0) {
                gc.setStroke(Color.web("#e2e8f0"));
                gc.strokeRect(x, baseY - 2, barW, 2);
            } else {
                gc.setFill(Color.web("#4361ee"));
                gc.fillRoundRect(x, barY, barW, barH, 6, 6);
                gc.setFill(Color.WHITE);
                gc.setFont(Font.font("Arial", FontWeight.BOLD, 11));
                gc.fillText(String.valueOf(values[i]), x + barW / 2 - 5, barY + 16);
            }
            gc.setFill(Color.web("#1b2a4a"));
            gc.setFont(Font.font("Arial", 11));
            gc.fillText(MONTHS[i], x + barW / 2 - 10, baseY + 16);
        }
    }

    public static void drawLineChart(GraphicsContext gc, double w, double h,
                                     int[] values, int maxVal) {
        double baseY = h - 30;
        double stepX = (w - 60) / (values.length - 1);

        gc.setStroke(Color.web("#e2e8f0"));
        gc.setLineWidth(1);
        for (int i = 0; i <= 4; i++) {
            double y = 10 + (baseY - 10) * i / 4;
            gc.strokeLine(50, y, w - 10, y);
            gc.setFill(Color.web("#718096"));
            gc.setFont(Font.font("Arial", 10));
            gc.fillText(String.valueOf((int)(maxVal * (4 - i) / 4)), 5, y + 4);
        }

        double[] xs = new double[values.length];
        double[] ys = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            xs[i] = 50 + i * stepX;
            ys[i] = baseY - (values[i] / (double) maxVal) * (baseY - 10);
        }

        gc.setStroke(Color.web("#4361ee"));
        gc.setLineWidth(2.5);
        for (int i = 0; i < values.length - 1; i++)
            gc.strokeLine(xs[i], ys[i], xs[i+1], ys[i+1]);

        for (int i = 0; i < values.length; i++) {
            gc.setFill(Color.web("#4361ee"));
            gc.fillOval(xs[i]-5, ys[i]-5, 10, 10);
            gc.setFill(Color.WHITE);
            gc.fillOval(xs[i]-3, ys[i]-3, 6, 6);
            gc.setFill(Color.web("#1b2a4a"));
            gc.setFont(Font.font("Arial", 11));
            gc.fillText(MONTHS[i], xs[i]-10, baseY+16);
        }
    }
}
