package com.comp2042.ui;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Enhanced UI component that displays an epic "Game Over" message.
 * Features neon glow effects, centered layout, and pulsing animation.
 */
public class GameOverPanel extends BorderPane {

    private final Label gameOverLabel;
    private final VBox container;
    private final Timeline pulseAnimation;

    public GameOverPanel() {
        // Create the main GAME OVER label
        gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");
        
        // Create subtitle
        Label subtitle = new Label("Press N for New Game");
        subtitle.getStyleClass().add("gameOverSubtitle");
        
        // Container with styling
        container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.getStyleClass().add("gameOverContainer");
        container.getChildren().addAll(gameOverLabel, subtitle);
        
        // Center the container
        setCenter(container);
        setMaxWidth(400);
        setMaxHeight(300);
        
        // Add pulsing glow animation
        pulseAnimation = createPulseAnimation();
        pulseAnimation.play();
        
        // Add visibility listener for fade-in effect
        visibleProperty().addListener((obs, wasVisible, isNowVisible) -> {
            if (isNowVisible) {
                playFadeIn();
            }
        });
    }

    /**
     * Creates a pulsing glow effect on the Game Over text.
     */
    private Timeline createPulseAnimation() {
        Timeline pulse = new Timeline(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(gameOverLabel.scaleXProperty(), 1.0),
                new KeyValue(gameOverLabel.scaleYProperty(), 1.0)
            ),
            new KeyFrame(Duration.millis(800), 
                new KeyValue(gameOverLabel.scaleXProperty(), 1.05, Interpolator.EASE_BOTH),
                new KeyValue(gameOverLabel.scaleYProperty(), 1.05, Interpolator.EASE_BOTH)
            ),
            new KeyFrame(Duration.millis(1600), 
                new KeyValue(gameOverLabel.scaleXProperty(), 1.0, Interpolator.EASE_BOTH),
                new KeyValue(gameOverLabel.scaleYProperty(), 1.0, Interpolator.EASE_BOTH)
            )
        );
        pulse.setCycleCount(Timeline.INDEFINITE);
        return pulse;
    }

    /**
     * Plays the fade-in animation when the panel becomes visible.
     */
    private void playFadeIn() {
        setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), this);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }
}

