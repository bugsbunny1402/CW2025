package com.comp2042.model;

import com.comp2042.util.MatrixOperations;

/**
 * Data transfer object containing all information needed to render the current game state.
 * Includes the active brick, its position, ghost piece location, next brick preview,
 * and held brick if applicable. This class uses defensive copying to maintain immutability
 * and prevent external modification of internal game state.
 * 
 * <p>The view data is passed from the model to the GUI controller for rendering each frame.
 * All matrix data is copied to prevent accidental modification that could corrupt game state.
 * 
 * @see SimpleBoard#getViewData()
 */
public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int ghostXPosition;
    private final int ghostYPosition;
    private final int[][] holdBrickData; // New Field

    /**
     * Constructs a ViewData object with complete rendering information.
     * 
     * @param brickData the 2D matrix of the current falling brick
     * @param xPosition the horizontal position of the brick (column)
     * @param yPosition the vertical position of the brick (row)
     * @param nextBrickData the 2D matrix of the next brick to spawn
     * @param ghostXPosition the horizontal position where the brick will land
     * @param ghostYPosition the vertical position where the brick will land
     * @param holdBrickData the 2D matrix of the held brick, or null if none
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int ghostXPosition, int ghostYPosition, int[][] holdBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.ghostXPosition = ghostXPosition;
        this.ghostYPosition = ghostYPosition;
        this.holdBrickData = holdBrickData;
    }

    /**
     * Returns a defensive copy of the current brick's shape matrix.
     * Each cell contains 0 for empty or 1-7 for colored blocks.
     * 
     * @return a copy of the brick data array
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Returns the horizontal position of the current brick on the board.
     * 
     * @return the column index of the brick
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Returns the vertical position of the current brick on the board.
     * 
     * @return the row index of the brick
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Returns a defensive copy of the next brick's shape matrix.
     * Used to display the piece preview to help players plan ahead.
     * 
     * @return a copy of the next brick data array
     */
    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    /**
     * Returns the horizontal landing position of the ghost piece.
     * The ghost shows where the current brick will land if dropped straight down.
     * 
     * @return the column index of the ghost piece
     */
    public int getGhostX() {
        return ghostXPosition;
    }

    /**
     * Returns the vertical landing position of the ghost piece.
     * The ghost shows where the current brick will land if dropped straight down.
     * 
     * @return the row index of the ghost piece
     */
    public int getGhostY() {
        return ghostYPosition;
    }

    /**
     * Returns a defensive copy of the held brick's shape matrix, or null if no brick is held.
     * Players can swap the current brick with the held brick once per piece placement.
     * 
     * @return a copy of the held brick data array, or null if no brick is stored
     */
    public int[][] getHoldBrickData() {
        // Return null if no brick is held
        if (holdBrickData == null) return null;
        return MatrixOperations.copy(holdBrickData);
    }
}
