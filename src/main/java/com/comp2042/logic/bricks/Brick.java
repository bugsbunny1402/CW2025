package com.comp2042.logic.bricks;

import java.util.List;

/**
 * Defines the contract for a Tetris game piece (tetromino).
 * Each brick has one or more rotation states represented as 2D matrices,
 * where each cell contains a color code.
 * 
 * <p>The seven standard Tetris pieces are:
 * <ul>
 *   <li>I-piece - straight line (2 rotations)</li>
 *   <li>O-piece - square (1 rotation)</li>
 *   <li>T-piece - T-shape (4 rotations)</li>
 *   <li>L-piece - L-shape (4 rotations)</li>
 *   <li>J-piece - reverse L (4 rotations)</li>
 *   <li>S-piece - zigzag right (2 rotations)</li>
 *   <li>Z-piece - zigzag left (2 rotations)</li>
 * </ul>
 * 
 * <p>Matrix format: Each rotation state is a 2D int array where:
 * <ul>
 *   <li>0 = empty cell (transparent)</li>
 *   <li>1-7 = colored block (brick's color code)</li>
 * </ul>
 * 
 * @see IBrick
 * @see OBrick
 * @see TBrick
 * @see LBrick
 * @see JBrick
 * @see SBrick
 * @see ZBrick
 */
public interface Brick {
    /**
     * Returns the list of all rotation states for this brick.
     * Each element is a 2D matrix representing one orientation.
     * The list order determines rotation sequence (index 0, 1, 2, etc.).
     * 
     * @return list of rotation matrices, never null
     */
    List<int[][]> getShapeMatrix();
}