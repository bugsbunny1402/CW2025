package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;

/**
 * Represents the core game board for Tetris.
 * <p>
 * This class acts as the Model. It stores the game grid, manages the falling brick,
 * handles collision detection, and implements key mechanics like Hold Piece,
 * Ghost Piece calculations, and Level progression.
 * </p>
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

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    /**
     * Calculates the position where the current brick would land if dropped instantly.
     * Used for rendering the Ghost Piece.
     *
     * @return The coordinate point of the ghost piece.
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

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getTotalLinesCleared() {
        return totalLinesCleared;
    }

    /**
     * Swaps the current brick with the held brick.
     */
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

    @Override
    public boolean createNewBrick() {
        currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(4, 0); // Fixed Y=0
        hasHeldThisTurn = false;
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

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

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
        holdBrick = null;
        hasHeldThisTurn = false;
        createNewBrick();
    }
}