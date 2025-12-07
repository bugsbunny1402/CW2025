package com.comp2042.ui;

import com.comp2042.model.ViewData;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Renderer: small helper that handles drawing and updating rectangles in the
 * gamePanel (background) and the brickPanel (current falling brick).
 *
 * This keeps GuiController focused on event handling and game flow.
 */
public class Renderer {

    private final int cellSize;

    public Renderer(int cellSize) {
        this.cellSize = cellSize;
    }

    /**
     * Initialise the displayMatrix and add transparent rectangles into the provided gamePanel.
     * Mirrors the previous init loop in GuiController for the background board.
     */
    public void initBackground(GridPane gamePanel, int[][] boardMatrix, Rectangle[][] displayMatrix) {
        // ensure arrays match
        int rows = boardMatrix.length;
        int cols = boardMatrix[0].length;
        for (int i = 2; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Rectangle rectangle = new Rectangle(cellSize, cellSize);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }
    }

    /**
     * Initialise the brick preview rectangles inside brickPanel.
     */
    public void initBrickPanel(GridPane brickPanel, ViewData brick, Rectangle[][] rectangles) {
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(cellSize, cellSize);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
    }

    /**
     * Update the layout position of the brickPanel (keeps old layout math).
     */
    public void updateBrickPanelPosition(GridPane gamePanel, GridPane brickPanel, ViewData brick) {
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * cellSize);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * cellSize);
    }

    /**
     * Update rectangle fills for the current brick preview.
     */
    public void refreshBrickRectangles(ViewData brick, Rectangle[][] rectangles) {
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
            }
        }
    }

    /**
     * Update the background rectangles from the board matrix.
     */
    public void refreshBackgroundRectangles(int[][] board, Rectangle[][] displayMatrix) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private Paint getFillColor(int i) {
        switch (i) {
            case 0: return Color.TRANSPARENT;
            case 1: return Color.AQUA;
            case 2: return Color.BLUEVIOLET;
            case 3: return Color.DARKGREEN;
            case 4: return Color.YELLOW;
            case 5: return Color.RED;
            case 6: return Color.BEIGE;
            case 7: return Color.BURLYWOOD;
            default: return Color.WHITE;
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }
}
