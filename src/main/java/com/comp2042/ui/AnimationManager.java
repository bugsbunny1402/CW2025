package com.comp2042.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.List;

/**
 * Manages visual animations for the game.
 * Extracted from GuiController to support Single Responsibility Principle.
 * Handles line clearing effects and other visual transitions.
 */
public class AnimationManager {

    /**
     * Animates the clearing of completed rows with a flash and fade effect.
     * 
     * @param displayMatrix The matrix of rectangles representing the game board
     * @param rowIndices The list of row indices to animate
     * @param callback The callback to execute after animation completes
     */
    public void animateLineClear(Rectangle[][] displayMatrix, List<Integer> rowIndices, Runnable callback) {
        if (rowIndices == null || rowIndices.isEmpty()) {
            if (callback != null) callback.run();
            return;
        }
        
        // Phase 1: Flash white
        flashWhite(displayMatrix, rowIndices);
        
        // Phase 2: Create swish effect timeline
        Timeline swishTimeline = createSwishTimeline(displayMatrix, rowIndices, callback);
        swishTimeline.setCycleCount(1);
        swishTimeline.play();
    }

    /**
     * Flashes the specified rows white as the initial phase of the animation.
     */
    private void flashWhite(Rectangle[][] displayMatrix, List<Integer> rowIndices) {
        for (int rowIndex : rowIndices) {
            if (rowIndex >= 2 && rowIndex < displayMatrix.length) {
                for (int j = 0; j < displayMatrix[rowIndex].length; j++) {
                    displayMatrix[rowIndex][j].setFill(Color.WHITE);
                }
            }
        }
    }

    /**
     * Creates the timeline for the swish effect (fade to cyan and disappear).
     */
    private Timeline createSwishTimeline(Rectangle[][] displayMatrix, List<Integer> rowIndices, Runnable callback) {
        Timeline timeline = new Timeline();
        
        // Step 1: White → Cyan with glow (150ms)
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(150), e -> 
            transitionToCyan(displayMatrix, rowIndices)
        ));
        
        // Step 2: Fade more (300ms)
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(300), e -> 
            fadeRows(displayMatrix, rowIndices, 0.3)
        ));
        
        // Step 3: Complete - reset opacity and execute callback (400ms)
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(400), e -> {
            resetOpacity(displayMatrix, rowIndices);
            if (callback != null) callback.run();
        }));
        
        return timeline;
    }

    /**
     * Transitions rows from white to cyan color with reduced opacity.
     */
    private void transitionToCyan(Rectangle[][] displayMatrix, List<Integer> rowIndices) {
        for (int rowIndex : rowIndices) {
            if (rowIndex >= 2 && rowIndex < displayMatrix.length) {
                for (int j = 0; j < displayMatrix[rowIndex].length; j++) {
                    Rectangle rect = displayMatrix[rowIndex][j];
                    rect.setFill(Color.web("#00FFFF")); // Cyan glow
                    rect.setOpacity(0.7);
                }
            }
        }
    }

    /**
     * Fades the specified rows to the given opacity level.
     */
    private void fadeRows(Rectangle[][] displayMatrix, List<Integer> rowIndices, double opacity) {
        for (int rowIndex : rowIndices) {
            if (rowIndex >= 2 && rowIndex < displayMatrix.length) {
                for (int j = 0; j < displayMatrix[rowIndex].length; j++) {
                    displayMatrix[rowIndex][j].setOpacity(opacity);
                }
            }
        }
    }

    /**
     * Resets the opacity of all rectangles in the specified rows to fully opaque.
     */
    private void resetOpacity(Rectangle[][] displayMatrix, List<Integer> rowIndices) {
        for (int rowIndex : rowIndices) {
            if (rowIndex >= 2 && rowIndex < displayMatrix.length) {
                for (int j = 0; j < displayMatrix[rowIndex].length; j++) {
                    displayMatrix[rowIndex][j].setOpacity(1.0);
                }
            }
        }
    }
}
