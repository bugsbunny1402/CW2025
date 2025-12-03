package com.comp2042;

import javax.swing.text.View;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
                updateLevelAndSpeed(); // Reuse logic
            }
            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    @Override
    public DownData onHardDropEvent(MoveEvent event) {
        // Implement Hard Drop by looping normal moveBrickDown
        boolean canMove = true;
        while (canMove) {
            canMove = board.moveBrickDown();
            if (canMove) {
                board.getScore().add(2); // Bonus score for hard dropping
            }
        }

        // Brick has landed
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();
        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
            updateLevelAndSpeed();
        }
        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }

        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        if (board instanceof SimpleBoard) {
            ((SimpleBoard) board).swapHoldBrick();
        }
        return board.getViewData();
    }

    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        // Reset Level and Speed for new game
        viewGuiController.setLevel(1);
        viewGuiController.setGameSpeed(400);
    }

    private void updateLevelAndSpeed() {
        if (board instanceof SimpleBoard) {
            int currentLevel = ((SimpleBoard) board).getCurrentLevel();
            viewGuiController.setLevel(currentLevel);

            // Calculate new speed: Base 400ms, faster by 30ms per level, capped at 100ms
            long newSpeed = 400 - ((long) (currentLevel - 1) * 30);
            newSpeed = Math.max(newSpeed, 100);
            viewGuiController.setGameSpeed(newSpeed);
        }
    }
}