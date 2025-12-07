package com.comp2042.util;

import java.io.*;

/**
 * Utility class for managing high score persistence.
 * Handles loading and saving the highest score to disk.
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
     * Loads the high score from file.
     * @return The saved high score, or 0 if file doesn't exist or error occurs.
     */
    public static int loadHighScore() {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(HIGHSCORE_FILE))) {
            return dis.readInt();
        } catch (IOException e) {
            return 0;
        }
    }

    /**
     * Saves the high score to file.
     * @param score The score to save.
     */
    public static void saveHighScore(int score) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(HIGHSCORE_FILE))) {
            dos.writeInt(score);
        } catch (IOException e) {
            System.err.println("Could not save high score: " + e.getMessage());
        }
    }
}
