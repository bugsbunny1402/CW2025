package com.comp2042.logic.bricks;

import com.comp2042.util.MatrixOperations;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents the I Tetris brick.
 */
final class IBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();
    /**
     * Constructs the brick and defines its rotation matrices.
     */
    public IBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
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