package com.comp2042;

import com.comp2042.util.HighScoreManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HighScoreManager utility class.
 * Tests high score persistence functionality.
 */
public class HighScoreManagerTest {

    private static final String HIGHSCORE_FILE = "highscore.dat";

    @AfterEach
    void cleanUp() {
        // Delete test file after each test
        File file = new File(HIGHSCORE_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void loadHighScore_shouldReturnZero_whenFileDoesNotExist() {
        int score = HighScoreManager.loadHighScore();
        assertEquals(0, score, "Should return 0 when high score file doesn't exist");
    }

    @Test
    void saveAndLoadHighScore_shouldPersistScore() {
        int testScore = 12345;
        
        HighScoreManager.saveHighScore(testScore);
        int loadedScore = HighScoreManager.loadHighScore();
        
        assertEquals(testScore, loadedScore, "Loaded score should match saved score");
    }

    @Test
    void saveHighScore_shouldOverwritePreviousScore() {
        HighScoreManager.saveHighScore(1000);
        HighScoreManager.saveHighScore(2000);
        
        int loadedScore = HighScoreManager.loadHighScore();
        
        assertEquals(2000, loadedScore, "Should overwrite previous score");
    }

    @Test
    void constructor_shouldThrowException() {
        // Utility class should not be instantiable
        try {
            java.lang.reflect.Constructor<HighScoreManager> constructor = 
                HighScoreManager.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            assertThrows(Exception.class, () -> {
                constructor.newInstance();
            }, "Utility class constructor should throw exception");
        } catch (NoSuchMethodException e) {
            // If there's no constructor, that's also fine
            assertTrue(true);
        }
    }
}
