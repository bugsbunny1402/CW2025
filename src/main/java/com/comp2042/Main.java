package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // LOAD START MENU (Not gameLayout.fxml)
        URL location = getClass().getClassLoader().getResource("startMenu.fxml");

        if (location == null) {
            System.err.println("Could not find startMenu.fxml!");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("TetrisJFX");
        // Window size for the menu
        Scene scene = new Scene(root, 520, 580);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}