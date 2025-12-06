package com.comp2042.logic.bricks;

import com.comp2042.MatrixOperations;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents the S Tetris brick.
 */
final class SBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();
    /**
     * Constructs the brick and defines its rotation matrices.
     */
    public SBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 5, 5, 0},
                {5, 5, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {5, 0, 0, 0},
                {5, 5, 0, 0},
                {0, 5, 0, 0},
                {0, 0, 0, 0}
        });
    }
    /**
     * Gets the shape matrices for this brick.
     *
     * @return A list of 2D arrays representing rotations.
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}