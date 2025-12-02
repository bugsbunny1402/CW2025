package com.comp2042;

public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    /**
     * Instantly drops the current brick to its lowest possible position.
     * @return The number of rows the brick was moved down.
     */
    int hardDrop();

    void newGame();
}
