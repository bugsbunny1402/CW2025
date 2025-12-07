package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

/**
 * Main entry point for the Tetris game application.
 * Extends JavaFX Application to provide the GUI framework.
 * This class initializes the application window and loads the start menu.
 * 
 * <p>The application follows this startup sequence:
 * <ol>
 *   <li>JavaFX platform initialization</li>
 *   <li>Load the start menu FXML layout</li>
 *   <li>Create and show the primary stage with start menu</li>
 *   <li>User selects "Start Game" to transition to game view</li>
 * </ol>
 * 
 * <p>Window properties:
 * <ul>
 *   <li>Title: "TetrisJFX"</li>
 *   <li>Dimensions: 520x580 pixels</li>
 *   <li>Not resizable</li>
 * </ul>
 * 
 * @see javafx.application.Application
 * @see StartMenuController
 */
public class Main extends Application {

    /**
     * Initializes and displays the primary application window.
     * Loads the start menu FXML and sets up the initial scene.
     * 
     * @param primaryStage the main application window provided by JavaFX
     * @throws Exception if FXML loading fails or other initialization errors occur
     */
    /**
     * Initializes and displays the primary application window.
     * Loads the start menu FXML and sets up the initial scene.
     * 
     * @param primaryStage the main application window provided by JavaFX
     * @throws Exception if FXML loading fails or other initialization errors occur
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
        Scene scene = new Scene(root, 520, 580);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Application entry point.
     * Launches the JavaFX application by calling the start method.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}