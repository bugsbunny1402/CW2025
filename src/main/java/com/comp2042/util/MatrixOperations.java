package com.comp2042.util;

import com.comp2042.model.ClearRow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
/**
 * A utility class providing static methods for matrix manipulations used in the game logic.
 * <p>
 *     This class handles low-level operations such as collision detection,
 *     matrix copying, merging grids, and checking for cleared rows.
 * </p>
 */

public class MatrixOperations {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private MatrixOperations(){
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    /**
     * Checks if a brick collides with existing blocks or board boundaries at a specific position.
     *
     * @param matrix The current game board matrix.
     * @param brick  The matrix representation of the brick shape.
     * @param x      The x-coordinate (column) of the brick's position.
     * @param y      The y-coordinate (row) of the brick's position.
     * @return true if an intersection (collision) is detected, false otherwise.
     */
    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0 && (checkOutOfBound(matrix, targetX, targetY) || matrix[targetY][targetX] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Helper method to check if coordinates are outside the board's dimensions.
     *
     * @param matrix  The game board matrix.
     * @param targetX The target x-coordinate.
     * @param targetY The target y-coordinate.
     * @return true if the coordinates are out of bounds, false otherwise.
     */
    private static boolean checkOutOfBound(int[][] matrix, int targetX, int targetY) {
        // outside vertically
        if (targetY < 0 || targetY >= matrix.length) {
            return true;
        }
        // outside horizontally
        if (targetX < 0 || targetX >= matrix[targetY].length) {
            return true;
        }
        return false;
    }
    /**
     * Creates a deep copy of a 2D integer array.
     *
     * @param original The original matrix to copy.
     * @return A new 2D array with the same contents as the original.
     */
    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] aMatrix = original[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }
    /**
     * Merges a brick into the game board matrix.
     * <p>
     *     This method is called when a brick lands. It transfers the brick's non-zero
     *     cells onto the board's grid.
     * </p>
     *
     * @param filledFields The current game board matrix.
     * @param brick        The brick shape to merge.
     * @param x            The x-coordinate of the brick.
     * @param y            The y-coordinate of the brick.
     * @return A new matrix representing the board after the merge.
     */
    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] copy = copy(filledFields);
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0) {
                    copy[targetY][targetX] = brick[j][i];
                }
            }
        }
        return copy;
    }
    /**
     * Scans the matrix for full rows, removes them, and shifts lines down.
     *
     * @param matrix The current game board matrix.
     * @return A ClearRow object containing the number of cleared lines, the new matrix, and the score.
     */
    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            int[] tmpRow = new int[matrix[i].length];
            boolean rowToClear = true;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rowToClear = false;
                }
                tmpRow[j] = matrix[i][j];
            }
            if (rowToClear) {
                clearedRows.add(i);
            } else {
                newRows.add(tmpRow);
            }
        }
        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] row = newRows.pollLast();
            if (row != null) {
                tmp[i] = row;
            } else {
                break;
            }
        }
        int scoreBonus = 50 * clearedRows.size() * clearedRows.size();
        return new ClearRow(clearedRows.size(), tmp, scoreBonus, clearedRows);
    }
    /**
     * Creates a deep copy of a list of 2D integer arrays.
     * Used for copying brick shapes.
     *
     * @param list The list of matrices to copy.
     * @return A new list containing deep copies of the matrices.
     */
    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}
