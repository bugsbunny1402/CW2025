package com.comp2042.model;

import com.comp2042.util.MatrixOperations;

/**
 * Contains information about a brick's next rotation orientation.
 * Used by collision detection to check if rotating is valid before
 * committing to the rotation change. This prevents invalid rotations
 * that would cause the brick to overlap with walls or existing blocks.
 * 
 * @see BrickRotator#getNextShape()
 */
public final class NextShapeInfo {
    private final int[][] shape;
    private final int position;
    /**
     * Creates a NextShapeInfo with shape data and rotation index.
     *
     * @param shape the 2D matrix of the next rotation orientation
     * @param position the index of this rotation in the brick's rotation list
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }
    /**
     * Returns a defensive copy of the next rotation's shape matrix.
     * 
     * @return a copy of the 2D shape array
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }
    /**
     * Returns the index of this rotation in the brick's rotation list.
     * 
     * @return the rotation index
     */
    public int getPosition() {
        return position;
    }
}
