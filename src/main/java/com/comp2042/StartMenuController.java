package com.comp2042;

import com.comp2042.controller.GameController;
import com.comp2042.controller.GuiController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller for the start menu screen of the Tetris game.
 * Handles navigation from the main menu to the game screen and application exit.
 * This controller is bound to the start menu FXML layout and responds
 * to user interactions with menu buttons.
 * 
 * <p>Responsibilities:
 * <ul>
 *   <li>Loading the game layout when "Start Game" is clicked</li>
 *   <li>Initializing game controllers and connecting logic to GUI</li>
 *   <li>Managing scene transitions between menu and game</li>
 *   <li>Handling application exit requests</li>
 * </ul>
 * 
 * @see com.comp2042.controller.GuiController
 * @see com.comp2042.controller.GameController
 */
public class StartMenuController {

    @FXML
    public void onStartGame(ActionEvent event) {
        try {
            // Load the Game Layout
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gameLayout.fxml"));
            Parent root = loader.load();

            // Initialize the Game Logic
            GuiController guiController = loader.getController();
            new GameController(guiController); // Connects Logic to GUI

            // Switch the Scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 520, 580);

            // Ensure focus is on the game panel so keys work immediately
            root.requestFocus();

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Exit Game" button click event.
     * Terminates the application cleanly.
     * 
     * @param event the button click event
     */
    @FXML
    public void onExitGame(ActionEvent event) {
        System.exit(0);
    }
}
