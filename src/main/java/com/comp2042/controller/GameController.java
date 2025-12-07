package com.comp2042.controller;

import com.comp2042.audio.SoundManager;
import com.comp2042.events.*;
import com.comp2042.model.*;

/**
 * Controls the main game logic and coordinates interaction between the game model and user interface.
 * This controller handles all player input events, manages game state transitions, and updates
 * the visual display accordingly. It follows the MVC pattern where this class acts as the
 * Controller layer, delegating data management to the Board and rendering to the GuiController.
 * 
 * <p>Key responsibilities include:
 * <ul>
 *   <li>Processing user input (movement, rotation, hard drop, hold)</li>
 *   <li>Managing brick placement and collision detection</li>
 *   <li>Calculating and applying score with combo bonuses</li>
 *   <li>Triggering visual and audio feedback</li>
 *   <li>Adjusting game difficulty through level progression</li>
 * </ul>
 * 
 * @see InputEventListener
 * @see Board
 * @see GuiController
 */
public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;
    private final SoundManager soundManager;

    /**
     * Constructs a new GameController and initializes the game environment.
     * Sets up the game board, audio system, and binds the view controller.
     * The background music begins playing automatically upon initialization.
     * 
     * @param c the GUI controller responsible for rendering the game view
     */
    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        soundManager = new SoundManager();
        soundManager.startMusic();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
    }

    /**
     * Handles downward movement of the active brick, either from user input or automatic gravity.
     * When the brick cannot move further down, it merges with the board and triggers line clearing.
     * If lines are cleared, calculates combo bonuses and plays animation effects.
     * Awards 1 point for user-initiated soft drops.
     * 
     * @param event the movement event containing the source (user or system timer)
     * @return DownData containing clear row information and updated view data
     */
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

    /**
     * Attempts to move the active brick one position to the left.
     * Movement is prevented if it would cause a collision with the board boundaries
     * or existing blocks.
     * 
     * @param event the movement event triggering this action
     * @return ViewData containing the updated brick position and game state
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    /**
     * Attempts to move the active brick one position to the right.
     * Movement is prevented if it would cause a collision with the board boundaries
     * or existing blocks.
     * 
     * @param event the movement event triggering this action
     * @return ViewData containing the updated brick position and game state
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    /**
     * Rotates the active brick 90 degrees counter-clockwise.
     * Rotation is prevented if the resulting position would collide with
     * the board boundaries or existing blocks.
     * 
     * @param event the rotation event triggering this action
     * @return ViewData containing the updated brick orientation and game state
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    /**
     * Executes a hard drop, instantly moving the brick to its lowest possible position.
     * Awards 2 points for each row the brick travels during the drop.
     * After landing, checks for line clears and handles combo scoring.
     * 
     * @param event the hard drop event triggering this action
     * @return DownData containing clear row information and updated view data
     */
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

    /**
     * Swaps the current falling brick with the held brick.
     * If no brick is currently held, stores the current brick and spawns a new one.
     * Can only be used once per brick placement to prevent exploitation.
     * 
     * @param event the hold event triggering this action
     * @return ViewData containing the updated brick and hold piece state
     */
    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        board.swapHoldBrick();
        return board.getViewData();
    }

    /**
     * Resets the game to its initial state, starting a new game session.
     * Clears the board, resets the score and level, and initializes the game speed.
     * The combo counter is also reset to zero.
     */
    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        // Reset Level and Speed for new game
        viewGuiController.setLevel(1);
        viewGuiController.setGameSpeed(400);
        viewGuiController.setCombo(0);
    }

    /**
     * Updates the game difficulty by adjusting the brick fall speed based on current level.
     * Speed increases by 30ms per level, starting at 400ms and capping at 100ms minimum.
     * This creates a progressive difficulty curve as the player clears more lines.
     */
    private void updateLevelAndSpeed() {
        int currentLevel = board.getCurrentLevel();
        viewGuiController.setLevel(currentLevel);

        // Calculate new speed: Base 400ms, faster by 30ms per level, capped at 100ms
        long newSpeed = 400 - ((long) (currentLevel - 1) * 30);
        newSpeed = Math.max(newSpeed, 100);
        viewGuiController.setGameSpeed(newSpeed);
    }
}
