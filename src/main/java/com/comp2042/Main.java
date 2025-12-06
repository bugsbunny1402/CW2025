package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
/**
 * The entry point for the Tetris application.
 * This class sets up the JavaFX Stage, loads the FXML layout, and initializes the game controller.
 */
public class Main extends Application {
    /**
     * Starts the JavaFX application by loading resources and setting up the primary stage.
     *
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     * @throws Exception If the FXML resource cannot be found or loaded.
     */
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
        Scene scene = new Scene(root, 450, 550);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    /**
     * The main method that launches the JavaFX application.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}