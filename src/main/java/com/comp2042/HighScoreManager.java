package com.comp2042;

import java.io.*;

public class HighScoreManager {

    private static final String HIGHSCORE_FILE = "highscore.dat";

    public static int loadHighScore() {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(HIGHSCORE_FILE))) {
            return dis.readInt();
        } catch (IOException e) {
            return 0;
        }
    }

    public static void saveHighScore(int score) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(HIGHSCORE_FILE))) {
            dos.writeInt(score);
        } catch (IOException e) {
            System.err.println("Could not save high score: " + e.getMessage());
        }
    }
}
