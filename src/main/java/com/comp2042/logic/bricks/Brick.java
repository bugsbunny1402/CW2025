package com.comp2042.logic.bricks;

import java.util.List;
/**
 * Represents a Tetris brick.
 */
public interface Brick {
    /**
     * Gets the list of shape matrices for all rotations of this brick.
     *
     * @return A list of 2D integer arrays.
     */
    List<int[][]> getShapeMatrix();
}