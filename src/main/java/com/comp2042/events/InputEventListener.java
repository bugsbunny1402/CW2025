package com.comp2042.events;

import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;

/**
 * Defines the contract for processing user input events in the game.
 * Implementations handle keyboard events by updating game state and returning
 * data for rendering. This interface separates input handling from the view layer.
 * 
 * <p>Event handling pattern:
 * <ol>
 *   <li>GUI layer captures keyboard input</li>
 *   <li>Creates a MoveEvent with current pause/game-over state</li>
 *   <li>Passes event to this listener</li>
 *   <li>Listener updates model and returns view data</li>
 *   <li>GUI renders the updated state</li>
 * </ol>
 * 
 * <p>Most methods return ViewData for simple moves (left, right, rotate),
 * while methods that may trigger line clears (down, hard drop) return DownData
 * which includes both ViewData and potential ClearRow information.
 * 
 * @see MoveEvent
 * @see ViewData
 * @see DownData
 * @see com.comp2042.controller.GameController
 */
public interface InputEventListener {
    /**
     * Processes a downward movement event (soft drop).
     * Moves piece down one row if possible, checking for collision and line clears.
     * 
     * @param event the move event containing game state flags
     * @return DownData with updated view and potential line clear results
     */
    DownData onDownEvent(MoveEvent event);
    /**
     * Processes a leftward movement event.
     * Attempts to shift the piece one column to the left.
     * 
     * @param event the move event containing game state flags
     * @return ViewData with updated piece position, or unchanged if blocked
     */
    ViewData onLeftEvent(MoveEvent event);
    /**
     * Processes a rightward movement event.
     * Attempts to shift the piece one column to the right.
     * 
     * @param event the move event containing game state flags
     * @return ViewData with updated piece position, or unchanged if blocked
     */
    ViewData onRightEvent(MoveEvent event);
    /**
     * Processes a rotation event.
     * Rotates the piece counter-clockwise if space permits.
     * 
     * @param event the move event containing game state flags
     * @return ViewData with updated piece orientation, or unchanged if blocked
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Processes a hard drop event (instant drop).
     * Immediately moves piece to its landing position and locks it in place.
     * Awards bonus points based on drop distance.
     * 
     * @param event the move event containing game state flags
     * @return DownData with final position and potential line clear results
     */
    DownData onHardDropEvent(MoveEvent event);

    /**
     * Processes a hold event.
     * Swaps the current piece with the held piece, or stores current if none held.
     * Can only be used once per piece placement.
     * 
     * @param event the move event containing game state flags
     * @return ViewData with the swapped piece
     */
    ViewData onHoldEvent(MoveEvent event);
    /**
     * Initiates a new game session.
     * Resets the board, clears score, and spawns the first piece.
     */
    void createNewGame();
}
