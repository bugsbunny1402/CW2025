package com.comp2042;
/**
 * Defines the types of brick movements.
 */
/**
 * Enumerates the different types of movement actions available in the game.
 */
public enum EventType {
    /** Moves the brick down by one row */
    DOWN,
    /** Moves the brick to the left. */
    LEFT,
    /** Moves the brick to the right. */
    RIGHT,
    /** Rotates the brick. */
    ROTATE,
    /** Instantly drops the brick to the bottom. */
    HARD_DROP,
    /** Swaps the current brick with the held brick. */
    HOLD
}