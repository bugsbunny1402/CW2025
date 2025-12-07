package com.comp2042.model;

/**
 * Defines the contract for a Tetris game board implementation.
 * Provides methods for brick movement, rotation, collision detection,
 * line clearing, and game state management.
 * 
 * <p>This interface allows for different board implementations while
 * maintaining a consistent API for controllers. It separates the game
 * logic from specific implementation details.
 * 
 * @see SimpleBoard
 */
public interface Board {
    /**
     * Attempts to move the active brick down by one row.
     * 
     * @return true if movement succeeded, false if blocked by collision
     */
    boolean moveBrickDown();
    /**
     * Attempts to move the active brick left by one column.
     * 
     * @return true if movement succeeded, false if blocked by collision
     */
    boolean moveBrickLeft();
    /**
     * Attempts to move the active brick right by one column.
     * 
     * @return true if movement succeeded, false if blocked by collision
     */
    boolean moveBrickRight();
    /**
     * Attempts to rotate the active brick counter-clockwise by 90 degrees.
     * 
     * @return true if rotation succeeded, false if blocked by collision
     */
    boolean rotateLeftBrick();
    /**
     * Spawns a new brick at the top of the board.
     * 
     * @return true if spawn causes immediate collision (game over), false otherwise
     */
    boolean createNewBrick();
    /**
     * Returns the 2D matrix representing the board's static blocks.
     * Does not include the currently falling brick.
     * 
     * @return the board grid array where 0 is empty and 1-7 are colored blocks
     */
    int[][] getBoardMatrix();
    /**
     * Compiles all data needed by the view to render the current frame.
     * 
     * @return ViewData containing brick positions, previews, and ghost piece info
     */
    ViewData getViewData();
    /**
     * Permanently merges the current brick into the board's static grid.
     * Called when a brick can no longer move down and becomes part of the playfield.
     */
    void mergeBrickToBackground();
    /**
     * Scans for and removes any complete horizontal lines from the board.
     * Lines above cleared rows drop down to fill the gap.
     * 
     * @return ClearRow containing line count, updated board, and score bonus
     */
    ClearRow clearRows();
    /**
     * Returns the score tracker for the current game session.
     * 
     * @return the Score instance managing points and combos
     */
    Score getScore();

    /**
     * Instantly drops the brick to its lowest possible position.
     * 
     * @return the number of rows the brick descended
     */
    int hardDrop();
    /**
     * Resets the board to start a fresh game with cleared grid and initial values.
     */
    void newGame();
    
    /**
     * Returns the current difficulty level based on lines cleared.
     * 
     * @return the current level number
     */
    int getCurrentLevel();
    
    /**
     * Swaps the active brick with the held piece, if hold mechanic is supported.
     */
    void swapHoldBrick();
}
