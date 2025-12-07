package com.comp2042;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

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

    @FXML
    public void onExitGame(ActionEvent event) {
        System.exit(0);
    }
}
