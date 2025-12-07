package com.comp2042;

import com.comp2042.model.ClearRow;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearRowTest {

    @Test
    void clearRow_shouldStoreLinesRemoved() {
        ClearRow clear = new ClearRow(2, new int[][]{{0}}, 100);

        assertEquals(2, clear.getLinesRemoved(),
                "ClearRow should store the correct number of removed lines");
    }

    @Test
    void clearRow_shouldReturnScoreBonus() {
        ClearRow clear = new ClearRow(1, new int[][]{{0}}, 50);

        assertEquals(50, clear.getScoreBonus(),
                "ClearRow should return the correct score bonus");
    }

    @Test
    void clearRow_shouldStoreNewMatrix() {
        int[][] newMatrix = {
                {0, 1},
                {2, 3}
        };

        ClearRow clear = new ClearRow(1, newMatrix, 50);

        assertArrayEquals(newMatrix, clear.getNewMatrix(),
                "ClearRow should store and return the provided matrix");
    }
}
