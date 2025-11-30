package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * RandomBrickGenerator
 *
 * This class is an implementation of {@link BrickGenerator} that selects
 * random bricks from the predefined brick list.
 *
 * Responsibilities:
 * - Maintain a queue of upcoming bricks (nextBricks)
 * - Provide the current brick when requested
 * - Ensure there is always at least one brick pre-generated
 *
 * Behaviour:
 * - Stores all seven standard Tetris bricks (I, J, L, O, S, T, Z)
 * - Uses ThreadLocalRandom to pick bricks uniformly at random
 * - Maintains a queue of two bricks: current brick + next preview brick
 *
 * This generator does NOT handle movement, rotation, or collision —
 * these responsibilities belong to SimpleBoard, BrickRotator and
 * MatrixOperations.
 */

public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;
    // All possible brick shapes available in the game (the 7 Tetris pieces)

    private final Deque<Brick> nextBricks = new ArrayDeque<>();
    // Queue storing the current and next bricks.
    // nextBricks.peek() = next preview brick
    // nextBricks.poll() = brick to spawn now

    /**
     * Creates the brick list containing all Tetris pieces
     * and pre-populates the queue with two random bricks.
     *
     * This guarantees the game always has:
     * - a current brick ready,
     * - a next preview brick available.
     */
    public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());
        nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
    }

    /**
     * Returns the next brick to be used in the game.
     * Also ensures the queue always contains at least one more brick.
     *
     * @return the brick to spawn on the board
     */
    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 1) {
            nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        }
        return nextBricks.poll();
    }

    /**
     * Returns the upcoming brick without removing it from the queue.
     *
     * Used for the "next brick" preview display in the GUI.
     *
     * @return the next brick, or null if queue is empty (should not happen)
     */
    @Override
    public Brick getNextBrick() {

        // Defensive programming – queue should never be empty
        if (nextBricks.isEmpty()) {
            nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        }

        return nextBricks.peek();
    }
}
