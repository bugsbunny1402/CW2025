package com.comp2042;
/**
 * Interface for components that listen to game input events.
 */
public interface InputEventListener {
    /**
     * Processes a DOWN event.
     * @param event The move event.
     * @return The result data.
     */
    DownData onDownEvent(MoveEvent event);
    /**
     * Processes a LEFT event.
     * @param event The move event.
     * @return The updated view data.
     */
    ViewData onLeftEvent(MoveEvent event);
    /**
     * Processes a RIGHT event.
     * @param event The move event.
     * @return The updated view data.
     */
    ViewData onRightEvent(MoveEvent event);
    /**
     * Processes a ROTATE event.
     * @param event The move event.
     * @return The updated view data.
     */
    ViewData onRotateEvent(MoveEvent event);

    DownData onHardDropEvent(MoveEvent event);

    ViewData onHoldEvent(MoveEvent event);
    /**
     * Starts a new game.
     */
    void createNewGame();
}