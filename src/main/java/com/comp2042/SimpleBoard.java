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
 *
 * The board itself is purely logical (no rendering). The GUI layers
 * (GuiController + Renderer) reads state from this class and displays it.
 *
 * This class is used through the Board interface by GameController.
 */
public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;

    // START: Level Progression Fields
    private int totalLinesCleared = 0;
    private int currentLevel = 1;
    // END: Level Progression Fields

    // Removed: private Label pauseLabel; and the corresponding import.

    /**
     * Calculates the lowest possible offset for the current brick without collision.
     * This is used to draw the ghost piece.
     * @return A Point representing the landing spot (x, y).
     */
    private Point calculateGhostOffset() {
        Point ghostOffset = new Point(currentOffset);
        int[][] currentShape = brickRotator.getCurrentShape();
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix); // Get a snapshot of the board

        // Continuously move down by 1 until the next move causes an intersection
        while (!MatrixOperations.intersect(currentMatrix, currentShape, (int) ghostOffset.getX(), (int) ghostOffset.getY() + 1)) {
            ghostOffset.translate(0, 1);
        }
        return ghostOffset;
    }

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    // START: Level Progression Getters
    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getTotalLinesCleared() {
        return totalLinesCleared;
    }
    // END: Level Progression Getters

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
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(4, 0);
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        Point ghost = calculateGhostOffset();
        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                brickGenerator.getNextBrick().getShapeMatrix().get(0),
                (int) ghost.getX(),
                (int) ghost.getY()
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
            // Level up for every 10 lines cleared
            int newLevel = (totalLinesCleared / 10) + 1;
            if (newLevel > currentLevel) {
                currentLevel = newLevel;
            }
        }

        return clearRow;
    }

    /**
     * Returns the current Score object for the board.
     */
    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public int hardDrop() {
        int distanceMoved = 0;

        //Start at the current position
        Point finalOffset = new Point(currentOffset);
        int[][] currentShape = brickRotator.getCurrentShape();
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix); // Copy the background matrix

        // Keep moving down one step at a time until the next step results in a conflict
        while (!MatrixOperations.intersect(currentMatrix, currentShape, (int) finalOffset.getX(), (int) finalOffset.getY() + 1)) {
            finalOffset.translate(0, 1);
            distanceMoved++;
        }

        if (distanceMoved > 0) {
            currentOffset = finalOffset; // Update the brick's position to its landing spot
        }

        return distanceMoved;
    }

    /**
     * Reset the board to its initial state for a new game:
     * - Clears the game matrix
     * - Resets the score
     * - Creates a new current brick
     */
    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();

        // RESET LEVEL STATE
        totalLinesCleared = 0;
        currentLevel = 1;

        createNewBrick();
    }
}