package com.comp2042.model;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.util.MatrixOperations;

import java.awt.*;

/**
 * Implementation of the game board that manages the Tetris play area and active pieces.
 * This class maintains the game grid state, handles brick movement and rotation,
 * and implements core Tetris mechanics including collision detection, line clearing,
 * and level progression.
 * 
 * <p>The board uses a 2D integer matrix where each cell value represents either
 * empty space (0) or a colored block (1-7). The board manages three key pieces:
 * <ul>
 *   <li>Current falling brick being controlled by the player</li>
 *   <li>Held brick that can be swapped with the current piece</li>
 *   <li>Next brick preview for player planning</li>
 * </ul>
 * 
 * <p>Additional features:
 * <ul>
 *   <li>Ghost piece calculation showing landing position</li>
 *   <li>Hold piece mechanic with one-use-per-piece restriction</li>
 *   <li>Progressive difficulty through level increases every 10 lines</li>
 * </ul>
 * 
 * @see Board
 * @see BrickRotator
 * @see Score
 */
public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private int totalLinesCleared = 0;
    private int currentLevel = 1;

    // Hold Piece Fields
    private Brick holdBrick = null;
    private Brick currentBrick;
    private boolean hasHeldThisTurn = false;

    /**
     * Constructs a new game board with specified dimensions.
     * Initializes an empty grid, brick generator, rotation controller, and scoring system.
     * 
     * @param width the number of rows in the game board
     * @param height the number of columns in the game board
     */
    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    /**
     * Calculates where the current brick would land if dropped straight down.
     * This position is used to render the ghost piece, giving players a visual
     * guide for piece placement. The calculation simulates gravity without
     * affecting the actual brick position.
     *
     * @return Point coordinates of the ghost piece position
     */
    private Point calculateGhostOffset() {
        Point ghostOffset = new Point(currentOffset);
        int[][] currentShape = brickRotator.getCurrentShape();
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);

        while (!MatrixOperations.intersect(currentMatrix, currentShape, (int) ghostOffset.getX(), (int) ghostOffset.getY() + 1)) {
            ghostOffset.translate(0, 1);
        }
        return ghostOffset;
    }

    /**
     * Returns the current difficulty level determined by total lines cleared.
     * Level increases by 1 for every 10 lines cleared, affecting game speed.
     * 
     * @return the current level number starting from 1
     */
    @Override
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Returns the cumulative count of lines cleared during the current game session.
     * This value is used to calculate the current level and track player progress.
     * 
     * @return total number of lines cleared since game start
     */
    public int getTotalLinesCleared() {
        return totalLinesCleared;
    }

    /**
     * Exchanges the current falling brick with the held brick.
     * If no brick is held, stores the current brick and generates a new one.
     * If a brick is already held, swaps them.
     * Can only be used once per brick to prevent excessive use.
     * The brick is reset to the spawn position after swapping.
     */
    @Override
    public void swapHoldBrick() {
        if (hasHeldThisTurn) return;

        if (holdBrick == null) {
            holdBrick = currentBrick;
            // Generate new brick manually to avoid resetting hold flag via createNewBrick
            currentBrick = brickGenerator.getBrick();
            brickRotator.setBrick(currentBrick);
            currentOffset = new Point(4, 0);
        } else {
            Brick temp = currentBrick;
            currentBrick = holdBrick;
            holdBrick = temp;

            brickRotator.setBrick(currentBrick);
            currentOffset = new Point(4, 0);
        }
        hasHeldThisTurn = true;
    }

    /**
     * Spawns a new brick at the top center of the board.
     * Resets the hold mechanic flag to allow using hold once for this piece.
     * 
     * @return true if the new brick immediately collides (game over condition), false otherwise
     */
    @Override
    public boolean createNewBrick() {
        currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(4, 0); // Fixed Y=0
        hasHeldThisTurn = false;
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Attempts to move the current brick down by one row.
     * Checks for collisions before moving. Used for both gravity and soft drops.
     * 
     * @return true if the brick successfully moved down, false if blocked
     */
    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Attempts to move the current brick left by one column.
     * Movement is blocked if it would cause collision with walls or existing blocks.
     * 
     * @return true if the brick successfully moved left, false if blocked
     */
    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Attempts to move the current brick right by one column.
     * Movement is blocked if it would cause collision with walls or existing blocks.
     * 
     * @return true if the brick successfully moved right, false if blocked
     */
    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Attempts to rotate the current brick 90 degrees counter-clockwise.
     * Rotation is prevented if the new orientation would overlap with
     * existing blocks or extend beyond the board boundaries.
     * 
     * @return true if the brick successfully rotated, false if blocked
     */
    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    /**
     * Returns the underlying 2D array representing the game board state.
     * Each cell contains 0 for empty or 1-7 for colored blocks.
     * The top 2 rows are typically hidden from view (spawn area).
     * 
     * @return the game board matrix
     */
    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    /**
     * Compiles all visual data needed by the GUI to render the current game state.
     * Includes the active brick, its position, ghost piece location, next brick preview,
     * and held brick if any.
     * 
     * @return ViewData object containing all rendering information
     */
    @Override
    public ViewData getViewData() {
        Point ghost = calculateGhostOffset();
        int[][] holdData = (holdBrick != null) ? holdBrick.getShapeMatrix().get(0) : null;

        // PASS ALL 7 ARGUMENTS (Including Ghost and Hold)
        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                brickGenerator.getNextBrick().getShapeMatrix().get(0),
                (int) ghost.getX(),
                (int) ghost.getY(),
                holdData
        );
    }

    /**
     * Permanently adds the current brick to the board at its current position.
     * This is called when a brick can no longer move down, locking it in place.
     * The brick's blocks become part of the static board grid.
     */
    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Scans the board for completed horizontal lines and removes them.
     * Lines above cleared rows drop down to fill the gaps. Updates the
     * total lines cleared count and increases level if threshold is reached.
     * The scoring bonus is calculated based on number of lines cleared simultaneously.
     * 
     * @return ClearRow object containing cleared line count, updated matrix, and score bonus
     */
    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();

        if (clearRow.getLinesRemoved() > 0) {
            totalLinesCleared += clearRow.getLinesRemoved();
            int newLevel = (totalLinesCleared / 10) + 1;
            if (newLevel > currentLevel) currentLevel = newLevel;
        }
        return clearRow;
    }

    /**
     * Returns the Score object tracking points and combo multipliers.
     * The score object is bound to the UI for automatic display updates.
     * 
     * @return the current Score instance
     */
    @Override
    public Score getScore() {
        return score;
    }

    /**
     * Instantly drops the current brick to its lowest possible position.
     * The brick descends until it collides with the bottom or another block.
     * Used internally by the hard drop feature to calculate bonus points.
     * 
     * @return the number of rows the brick dropped
     */
    @Override
    public int hardDrop() {
        int distanceMoved = 0;
        Point finalOffset = new Point(currentOffset);
        int[][] currentShape = brickRotator.getCurrentShape();
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);

        while (!MatrixOperations.intersect(currentMatrix, currentShape, (int) finalOffset.getX(), (int) finalOffset.getY() + 1)) {
            finalOffset.translate(0, 1);
            distanceMoved++;
        }

        if (distanceMoved > 0) {
            currentOffset = finalOffset;
        }
        return distanceMoved;
    }

    /**
     * Resets the board to initial state for a new game.
     * Clears all blocks from the grid, resets score and level to starting values,
     * removes any held brick, and spawns the first piece.
     */
    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        totalLinesCleared = 0;
        currentLevel = 1;
        holdBrick = null;
        hasHeldThisTurn = false;
        createNewBrick();
    }
}
