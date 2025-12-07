package com.comp2042;

import com.comp2042.model.Score;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreTest {

    @Test
    void newScore_shouldStartAtZero() {
        Score score = new Score();
        assertEquals(0, score.scoreProperty().get(), "Score should start at 0");
    }

    @Test
    void add_shouldIncreaseScoreValue() {
        Score score = new Score();
        score.add(50);
        assertEquals(50, score.scoreProperty().get(), "Score should increase by 50");

        score.add(30);
        assertEquals(80, score.scoreProperty().get(), "Score should be 80 after adding another 30");
    }

    @Test
    void reset_shouldSetScoreToZero() {
        Score score = new Score();
        score.add(100);
        score.reset();
        assertEquals(0, score.scoreProperty().get(), "Score should reset to 0");
    }
}
