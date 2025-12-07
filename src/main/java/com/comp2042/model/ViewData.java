package com.comp2042.model;

import com.comp2042.util.MatrixOperations;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int ghostXPosition;
    private final int ghostYPosition;
    private final int[][] holdBrickData; // New Field

    // Updated Constructor
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int ghostXPosition, int ghostYPosition, int[][] holdBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.ghostXPosition = ghostXPosition;
        this.ghostYPosition = ghostYPosition;
        this.holdBrickData = holdBrickData;
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    public int getGhostX() {
        return ghostXPosition;
    }

    public int getGhostY() {
        return ghostYPosition;
    }

    public int[][] getHoldBrickData() {
        // Return null if no brick is held
        if (holdBrickData == null) return null;
        return MatrixOperations.copy(holdBrickData);
    }
}
