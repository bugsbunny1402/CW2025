package com.comp2042.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

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
     * Constructs the Game Over panel with a centered label.
     * The label's visual style is controlled by the CSS class "gameOverStyle".
     */
    public GameOverPanel() {
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");
        setCenter(gameOverLabel);
    }

}
