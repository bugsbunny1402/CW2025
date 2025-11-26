package com.comp2042.logic.bricks;

/**
 * BrickGenerator
 *
 * This interface defines how bricks are created for the Tetris game.
 *
 * Responsibilities:
 * - Provide the current brick that should be dropped onto the board.
 * - Provide the next brick (used for preview or internal queueing).
 *
 * Implementations:
 * - RandomBrickGenerator: selects bricks randomly.
 *  *
 *  * This generator is used by SimpleBoard to create new bricks whenever
 *  * the previous brick locks into place.
 *
 */

public interface BrickGenerator {

    Brick getBrick();

    Brick getNextBrick();
}
