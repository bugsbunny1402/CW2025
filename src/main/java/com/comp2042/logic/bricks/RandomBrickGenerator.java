package com.comp2042.logic.bricks;

import java.util.*;

/**
 * RandomBrickGenerator
 *
 * Refactored to use the "7-Bag Randomizer" system (Standard Tetris RNG).
 *
 * Behaviour:
 * - Instead of picking purely random numbers (which can result in drought/flood),
 * this generator puts one of each of the 7 pieces into a "bag".
 * - The bag is shuffled.
 * - Pieces are drawn from the bag until it is empty.
 * - When empty, a new bag of 7 distinct pieces is created and shuffled.
 *
 * This ensures fair gameplay where players are guaranteed to see every piece
 * at least once every 7 turns.
 */
public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickPrototypes; // Templates for the 7 bricks
    private final Deque<Brick> brickBag = new ArrayDeque<>(); // The "Bag"
    private final Deque<Brick> nextBricks = new ArrayDeque<>(); // The preview queue
    /**
     * Constructs the generator and initializes the list of available bricks.
     */
    public RandomBrickGenerator() {
        brickPrototypes = new ArrayList<>();
        brickPrototypes.add(new IBrick());
        brickPrototypes.add(new JBrick());
        brickPrototypes.add(new LBrick());
        brickPrototypes.add(new OBrick());
        brickPrototypes.add(new SBrick());
        brickPrototypes.add(new TBrick());
        brickPrototypes.add(new ZBrick());

        // Initialize by filling the preview queue
        // We need at least 2 bricks: one to play immediately, one for "Next" preview
        while (nextBricks.size() < 2) {
            nextBricks.add(getNextFromBag());
        }
    }

    /**
     * Pulls the next brick from the bag.
     * If the bag is empty, refilled it with a new shuffled set of 7 bricks.
     */
    private Brick getNextFromBag() {
        if (brickBag.isEmpty()) {
            List<Brick> newBag = new ArrayList<>(brickPrototypes);
            Collections.shuffle(newBag); // Shuffle the 7 pieces
            brickBag.addAll(newBag);
        }
        return brickBag.poll();
    }

    /**
     * Returns the next brick to be used in the game.
     * Ensures the preview queue stays populated.
     */
    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 1) {
            nextBricks.add(getNextFromBag());
        }
        return nextBricks.poll();
    }

    /**
     * Returns the upcoming brick for the preview window.
     */
    @Override
    public Brick getNextBrick() {
        if (nextBricks.isEmpty()) {
            nextBricks.add(getNextFromBag());
        }
        return nextBricks.peek();
    }
}
