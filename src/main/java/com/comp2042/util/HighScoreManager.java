package com.comp2042.util;

import java.io.*;

/**
 * Manages persistent storage and retrieval of the player's high score.
 * Uses a simple binary file to store the highest score achieved across game sessions.
 * The score file is saved in the same directory as the application for easy access.
 * 
 * <p>File name: {@code highscore.dat}
 * 
 * <p>Key features:
 * <ul>
 *   <li>Returns 0 if no high score file exists yet</li>
 *   <li>Binary format using DataInputStream/DataOutputStream</li>
 *   <li>Graceful error handling with fallback values</li>
 * </ul>
 * 
 * <p>This utility class cannot be instantiated.
 * 
 * @see com.comp2042.model.Score
 */
public class HighScoreManager {

    private static final String HIGHSCORE_FILE = "highscore.dat";

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private HighScoreManager() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Retrieves the current high score from persistent storage.
     * 
     * @return the stored high score, or 0 if file doesn't exist or read error occurs
     */
    public static int loadHighScore() {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(HIGHSCORE_FILE))) {
            return dis.readInt();
        } catch (IOException e) {
            return 0;
        }
    }

    /**
     * Saves a score to persistent storage.
     * Writes the score to the high score file, overwriting any previous value.
     * 
     * @param score the score to save
     */
    public static void saveHighScore(int score) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(HIGHSCORE_FILE))) {
            dos.writeInt(score);
        } catch (IOException e) {
            System.err.println("Could not save high score: " + e.getMessage());
        }
    }
}
