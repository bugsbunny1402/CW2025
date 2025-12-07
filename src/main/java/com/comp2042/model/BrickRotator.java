package com.comp2042.model;

import com.comp2042.logic.bricks.Brick;

/**
 * Manages the rotation logic for the active brick.
 */
public final class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    public BrickRotator() {
    }

    /**
     * Assigns a new brick to the rotator and resets the rotation index.
     *
     * @param brick The {@link Brick} to manage.
     */
    public void setBrick(Brick brick) {
        if (brick == null) {
            throw new IllegalArgumentException("brick must not be null");
        }
        this.brick = brick;
        this.currentShape = 0;
    }
    /**
     * Gets the matrix for the current shape rotation.
     *
     * @return The 2D array of the current shape.
     */
    public int[][] getCurrentShape() {
        ensureBrickSet();
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Calculates the next rotation state of the current brick.
     *
     * @return A {@link NextShapeInfo} object containing the next shape and its index.
     */
    public NextShapeInfo getNextShape() {
        ensureBrickSet();
        int nextShape = (currentShape + 1) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Sets the index of the current rotation state.
     *
     * @param currentShape The index to set.
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

    private void ensureBrickSet() {
        if (this.brick == null) {
            throw new IllegalStateException("Brick not set. Call setBrick() before using this class.");
        }
    }
}
