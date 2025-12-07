package com.comp2042.model;

/**
 * Combines the results of a downward movement with updated view data.
 * Returned when a brick moves down or is hard dropped, providing both
 * line clearing information and the new game state for rendering.
 * 
 * <p>This immutable object allows the controller to handle both
 * scoring (from ClearRow) and rendering (from ViewData) in one response.
 * 
 * @see ClearRow
 * @see ViewData
 */
public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;

    /**
     * Constructs a DownData result combining clear and view information.
     * 
     * @param clearRow the result of checking for line clears, or null if brick still falling
     * @param viewData the updated view state for rendering
     */
    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    /**
     * Returns the line clearing result, or null if no lines were cleared.
     * 
     * @return the ClearRow object or null
     */
    public ClearRow getClearRow() {
        return clearRow;
    }

    /**
     * Returns the updated view data for rendering the current game state.
     * 
     * @return the ViewData object containing rendering information
     */
    public ViewData getViewData() {
        return viewData;
    }
}
