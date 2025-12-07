package com.comp2042.model;

import com.comp2042.logic.bricks.Brick;

/**
 * Manages rotation state for the currently active Tetris brick.
 * Each brick type has multiple rotation states (1-4 depending on piece symmetry).
 * This class tracks which rotation is currently active and calculates the next rotation.
 * 
 * <p>The rotator maintains an index into the brick's shape list and provides
 * methods to retrieve current and next orientations. It validates that a brick
 * is set before any operations and handles index wrapping for seamless rotation.
 * 
 * @see Brick
 * @see NextShapeInfo
 */
public final class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Constructs a new BrickRotator with no brick assigned.
     * A brick must be set via setBrick() before using rotation methods.
     */
    public BrickRotator() {
    }

    /**
     * Assigns a brick to manage and resets rotation to the default orientation.
     * 
     * @param brick the brick whose rotation will be controlled
     * @throws IllegalArgumentException if brick is null
     */
    public void setBrick(Brick brick) {
        if (brick == null) {
            throw new IllegalArgumentException("brick must not be null");
        }
        this.brick = brick;
        this.currentShape = 0;
    }
    /**
     * Returns the matrix representing the brick's current rotation state.
     * 
     * @return 2D array of the current shape
     * @throws IllegalStateException if no brick has been set
     */
    public int[][] getCurrentShape() {
        ensureBrickSet();
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Calculates what the brick would look like after one rotation.
     * Does not actually perform the rotation; used for collision checking
     * before committing to the rotation.
     * 
     * @return NextShapeInfo containing the next shape matrix and its index
     * @throws IllegalStateException if no brick has been set
     */
    public NextShapeInfo getNextShape() {
        ensureBrickSet();
        int nextShape = (currentShape + 1) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Sets the current rotation index directly.
     * The index is normalized to fit within valid range, allowing negative values
     * which wrap to the end of the rotation list.
     * 
     * @param currentShape the rotation index to set
     * @throws IllegalStateException if no brick has been set
     */
    public void setCurrentShape(int currentShape) {
        ensureBrickSet();
        int size = brick.getShapeMatrix().size();
        if (size == 0) {
            this.currentShape = 0;
        } else {
            // normalize to [0, size-1]
            this.currentShape = ((currentShape % size) + size) % size;
        }
    }

    /**
     * Validates that a brick has been assigned before operations.
     * 
     * @throws IllegalStateException if setBrick() has not been called
     */
    private void ensureBrickSet() {
        if (this.brick == null) {
            throw new IllegalStateException("Brick not set. Call setBrick() before using this class.");
        }
    }
}
