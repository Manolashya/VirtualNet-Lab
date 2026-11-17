package com.virtualnetlab;

import com.virtualnetlab.ui.HomeScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        HomeScreen home = new HomeScreen(stage);
        Scene scene = new Scene(home.getView(), 1100, 720);
        stage.setTitle("VirtualNet Lab | CNIP Learning Simulator");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
