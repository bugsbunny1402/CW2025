package com.comp2042;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int ghostX;
    private final int ghostY;
    private final int[][] holdBrickData;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int ghostX, int ghostY, int[][] holdBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.ghostX = ghostX;
        this.ghostY = ghostY;
        this.holdBrickData = holdBrickData;
    }

    public int[][] getBrickData() { return MatrixOperations.copy(brickData); }
    public int getxPosition() { return xPosition; }
    public int getyPosition() { return yPosition; }
    public int[][] getNextBrickData() { return MatrixOperations.copy(nextBrickData); }

    public int getGhostX() { return ghostX; }
    public int getGhostY() { return ghostY; }

    public int[][] getHoldBrickData() {
        if (holdBrickData == null) return null;
        return MatrixOperations.copy(holdBrickData);
    }
}