package com.comp2042;
/**
 * Stores information about a brick's next rotation shape.
 */
public final class NextShapeInfo {
    private final int[][] shape;
    private final int position;
    /**
     * Constructs a new NextShapeInfo object.
     *
     * @param shape    The shape matrix.
     * @param position The rotation index.
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }
    /**
     * Gets the shape matrix.
     *
     * @return The 2D array.
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }
    /**
     * Gets the rotation position index.
     *
     * @return The index.
     */
    public int getPosition() {
        return position;
    }
}