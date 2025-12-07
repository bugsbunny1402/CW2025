package com.comp2042;

import javax.swing.text.View;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;
    private final SoundManager soundManager;

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        soundManager = new SoundManager();
        soundManager.startMusic();
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
            viewGuiController.refreshGameBackground(board.getBoardMatrix()); // Show merged piece

            clearRow = board.clearRows();

            if (clearRow.getLinesRemoved() > 0) {
                // Increment combo
                board.getScore().incrementCombo();
                int comboCount = board.getScore().getComboCount();
                int comboBonus = board.getScore().getComboBonus(clearRow.getScoreBonus());
                boolean isCombo = comboCount > 0;
                
                // Trigger line clear animation BEFORE updating board
                final ClearRow finalClearRow = clearRow;
                final int totalScore = clearRow.getScoreBonus() + comboBonus;
                viewGuiController.animateLineClear(clearRow.getClearedRowIndices(), () -> {
                    // After animation completes, update the board
                    viewGuiController.refreshGameBackground(board.getBoardMatrix());
                    board.getScore().add(totalScore);
                    
                    // Show score notification with combo effect
                    viewGuiController.showScoreNotification(totalScore, isCombo);
                    viewGuiController.setCombo(comboCount);
                    
                    soundManager.playClear();
                    updateLevelAndSpeed();
                });
            } else {
                // Reset combo if no lines cleared
                board.getScore().resetCombo();
                viewGuiController.setCombo(0);
            }

            if (board.createNewBrick()) {
                if (soundManager != null) {
                    soundManager.playGameOver();
                }
                viewGuiController.gameOver();
            }

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
        viewGuiController.refreshGameBackground(board.getBoardMatrix()); // Show merged piece
        
        ClearRow clearRow = board.clearRows();
        
        if (clearRow.getLinesRemoved() > 0) {
            // Increment combo
            board.getScore().incrementCombo();
            int comboCount = board.getScore().getComboCount();
            int comboBonus = board.getScore().getComboBonus(clearRow.getScoreBonus());
            boolean isCombo = comboCount > 0;
            
            // Trigger line clear animation BEFORE updating board
            final ClearRow finalClearRow = clearRow;
            final int totalScore = clearRow.getScoreBonus() + comboBonus;
            viewGuiController.animateLineClear(clearRow.getClearedRowIndices(), () -> {
                // After animation completes, update the board
                viewGuiController.refreshGameBackground(board.getBoardMatrix());
                board.getScore().add(totalScore);
                
                // Show score notification with combo effect
                viewGuiController.showScoreNotification(totalScore, isCombo);
                viewGuiController.setCombo(comboCount);
                
                updateLevelAndSpeed();
            });
            soundManager.playClear();
        } else {
            // Reset combo if no lines cleared
            board.getScore().resetCombo();
            viewGuiController.setCombo(0);
        }
        
        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }

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
        viewGuiController.setCombo(0);
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
