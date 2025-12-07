package com.comp2042.model;

/**
 * Defines the contract for the game board, handling movement and state.
 */
public interface Board {
    /**
     * Moves the brick down.
     * @return true if successful, false otherwise.
     */
    boolean moveBrickDown();
    /**
     * Moves the brick left.
     * @return true if successful, false otherwise.
     */
    boolean moveBrickLeft();
    /**
     * Moves the brick right.
     * @return true if successful, false otherwise.
     */
    boolean moveBrickRight();
    /**
     * Rotates the brick.
     * @return true if successful, false otherwise.
     */
    boolean rotateLeftBrick();
    /**
     * Creates a new brick.
     * @return true if successful, false if game over.
     */
    boolean createNewBrick();
    /**
     * Gets the board matrix.
     * @return The 2D grid array.
     */
    int[][] getBoardMatrix();
    /**
     * Gets view data for rendering.
     * @return The {@link ViewData}.
     */
    ViewData getViewData();
    /**
     * Merges the brick into the board.
     */
    void mergeBrickToBackground();
    /**
     * Clears full rows.
     * @return The {@link ClearRow} result.
     */
    ClearRow clearRows();
    /**
     * Gets the score object.
     * @return The {@link Score}.
     */
    Score getScore();

    /**
     * Instantly drops the current brick to its lowest possible position.
     * @return The number of rows the brick was moved down.
     */
    int hardDrop();
    /**
     * Starts a new game.
     */
    void newGame();
}
