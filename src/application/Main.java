package application;

import javafx.application.Application;
import javafx.stage.Stage;
import view.EntryView;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        EntryView.show(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
