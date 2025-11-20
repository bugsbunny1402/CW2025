package com.comp2042;

import com.comp2042.logic.bricks.Brick;


public final class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    public BrickRotator() {

    }


    public void setBrick(Brick brick) {
        if (brick == null) {
            throw new IllegalArgumentException("brick must not be null");
        }
        this.brick = brick;
        this.currentShape = 0;
    }

    public int[][] getCurrentShape() {
        ensureBrickSet();
        return brick.getShapeMatrix().get(currentShape);
    }


    public NextShapeInfo getNextShape() {
        ensureBrickSet();
        int nextShape = (currentShape + 1) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }


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
