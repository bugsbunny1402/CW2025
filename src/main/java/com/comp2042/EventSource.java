package com.comp2042;
/**
 * Defines the sources of movement events.
 */
/**
 * Enumerates the possible sources of a game event.
 */
public enum EventSource {
    /** The event was triggered by the player (e.g., keyboard input). */
    USER,
    /** The event was triggered by the game loop timer (automatic gravity). */
    THREAD
}