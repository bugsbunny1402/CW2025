package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;

/**
 * SimpleBoard
 *
 * This class represents the main game board for the game.
 * It is responsible for :
 * - Storing the current game matrix (placed bricks and empty cells)
 * - Tracking the current falling brick and its offset on the board
 * - Creating new bricks via a BrickGenerator
 * - Checking collisions and merging bricks into the background matrix
 * - Clearing full rows and updating the score
 * - Managing the Hold Piece mechanic
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
    private Brick currentBrick; // Promoted to field to allow swapping
    private boolean hasHeldThisTurn = false;

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    private Point calculateGhostOffset() {
        Point ghostOffset = new Point(currentOffset);
        int[][] currentShape = brickRotator.getCurrentShape();
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);

        while (!MatrixOperations.intersect(currentMatrix, currentShape, (int) ghostOffset.getX(), (int) ghostOffset.getY() + 1)) {
            ghostOffset.translate(0, 1);
        }
        return ghostOffset;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getTotalLinesCleared() {
        return totalLinesCleared;
    }

    /**
     * Swaps the current brick with the held brick.
     * If no brick is held, the current brick is moved to hold and a new one is generated.
     */
    public void swapHoldBrick() {
        if (hasHeldThisTurn) {
            return; // Prevent multiple swaps in one turn
        }

        if (holdBrick == null) {
            // First hold: Store current, spawn next immediately
            holdBrick = currentBrick;

            // Manually generate next brick to avoid resetting hasHeldThisTurn
            currentBrick = brickGenerator.getBrick();
            brickRotator.setBrick(currentBrick);
            currentOffset = new Point(4, 0);
        } else {
            // Swap current and hold
            Brick temp = currentBrick;
            currentBrick = holdBrick;
            holdBrick = temp;

            brickRotator.setBrick(currentBrick);
            currentOffset = new Point(4, 0);
        }

        hasHeldThisTurn = true; // Lock holding until next piece spawns
    }

    @Override
    public boolean createNewBrick() {
        currentBrick = brickGenerator.getBrick(); // Use the class field
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(4, 0);

        hasHeldThisTurn = false; // Reset hold flag for the new turn

        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

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

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        Point ghost = calculateGhostOffset();

        // Prepare hold data (null if empty)
        int[][] holdData = (holdBrick != null) ? holdBrick.getShapeMatrix().get(0) : null;

        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                brickGenerator.getNextBrick().getShapeMatrix().get(0),
                (int) ghost.getX(),
                (int) ghost.getY(),
                holdData // Pass Hold Data to View
        );
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();

        int linesRemoved = clearRow.getLinesRemoved();
        if (linesRemoved > 0) {
            totalLinesCleared += linesRemoved;
            int newLevel = (totalLinesCleared / 10) + 1;
            if (newLevel > currentLevel) {
                currentLevel = newLevel;
            }
        }
        return clearRow;
    }

    @Override
    public Score getScore() {
        return score;
    }

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

    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        totalLinesCleared = 0;
        currentLevel = 1;
        holdBrick = null; // Reset hold
        hasHeldThisTurn = false;
        createNewBrick();
    }
}