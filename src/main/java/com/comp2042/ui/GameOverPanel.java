package com.comp2042.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * UI component displaying the "Game Over" message when the game ends.
 * This panel overlays the game board and notifies the player that the game
 * has finished. The styling is defined in the window_style.css file under
 * the "gameOverStyle" class.
 * 
 * <p>The panel is shown when pieces can no longer be placed on the board,
 * and hidden when a new game starts.
 * 
 * @see GuiController
 */
public class GameOverPanel extends BorderPane {

    /**
     * Constructs the Game Over panel with a centered label and instructions.
     * The label's visual style is controlled by the CSS class "gameOverStyle".
     */
    public GameOverPanel() {
        // Create the main "GAME OVER" label
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");
        gameOverLabel.setWrapText(false);
        
        // Create the instruction label
        final Label instructionLabel = new Label("Press N for New Game");
        instructionLabel.getStyleClass().add("gameOverInstruction");
        instructionLabel.setWrapText(false);
        
        // Container for both labels with minimal spacing
        VBox content = new VBox(8);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(gameOverLabel, instructionLabel);
        
        setCenter(content);
        
        // Add CSS class for panel styling
        getStyleClass().add("game-over-panel");
        
        // Wide horizontal panel sizing
        setMinWidth(420);
        setMinHeight(140);
        setPrefWidth(420);
        setPrefHeight(140);
        setMaxWidth(420);
        setMaxHeight(140);
    }

}
