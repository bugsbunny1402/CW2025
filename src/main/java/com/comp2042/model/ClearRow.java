package com.comp2042.model;

import com.comp2042.util.MatrixOperations;

import java.util.List;

/**
 * Encapsulates the result of clearing completed lines from the game board.
 * Contains the number of lines removed, the updated board state, scoring information,
 * and the indices of cleared rows for animation purposes.
 * 
 * <p>This immutable data object is returned after checking for line clears,
 * allowing the controller to handle scoring, animations, and board updates
 * in a coordinated manner.
 * 
 * @see MatrixOperations#checkRemoving(int[][])
 */
public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;
    private final List<Integer> clearedRowIndices; // NEW: Which rows were cleared

    /**
     * Constructs a ClearRow result with line clearing information.
     * 
     * @param linesRemoved the number of complete lines that were removed
     * @param newMatrix the updated board grid after removing cleared lines
     * @param scoreBonus the base points awarded for this line clear
     * @param clearedRowIndices list of row indices that were cleared (for animations)
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus, List<Integer> clearedRowIndices) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
        this.clearedRowIndices = clearedRowIndices;
    }

    /**
     * Returns the count of lines that were cleared.
     * 
     * @return number of lines removed (0 if no lines were complete)
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Returns a defensive copy of the updated game board matrix.
     * The returned array has cleared lines removed and remaining lines shifted down.
     * 
     * @return a copy of the new board state
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Returns the base score bonus for this line clear before combo multipliers.
     * Calculated as 50 * (lines cleared)^2 in the standard implementation.
     * 
     * @return the base score points earned
     */
    public int getScoreBonus() {
        return scoreBonus;
    }

    /**
     * Returns the list of row indices that were cleared.
     * Used by the animation system to highlight cleared rows before removal.
     * 
     * @return list of cleared row indices (empty if no clears)
     */
    public List<Integer> getClearedRowIndices() {
        return clearedRowIndices;
    }
}
