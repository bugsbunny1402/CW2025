package com.comp2042;

import com.comp2042.model.ViewData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ViewData class.
 * Tests data encapsulation and getter methods.
 */
public class ViewDataTest {

    @Test
    void constructor_shouldStoreAllValues() {
        int[][] brickData = {{1, 1}, {1, 1}};
        int[][] nextData = {{2, 2}, {2, 2}};
        int[][] holdData = {{3, 3}, {3, 3}};
        
        ViewData viewData = new ViewData(brickData, 5, 10, nextData, 5, 20, holdData);
        
        assertEquals(5, viewData.getxPosition());
        assertEquals(10, viewData.getyPosition());
        assertEquals(5, viewData.getGhostX());
        assertEquals(20, viewData.getGhostY());
    }

    @Test
    void getBrickData_shouldReturnCopy_notOriginal() {
        int[][] original = {{1, 1}, {0, 0}};
        ViewData viewData = new ViewData(original, 0, 0, null, 0, 0, null);
        
        int[][] retrieved = viewData.getBrickData();
        
        // Modify retrieved data
        retrieved[0][0] = 99;
        
        // Original in ViewData should remain unchanged
        int[][] retrievedAgain = viewData.getBrickData();
        assertEquals(1, retrievedAgain[0][0], "ViewData should return a copy, not the original array");
    }

    @Test
    void getHoldBrickData_shouldReturnNull_whenNoHoldBrick() {
        ViewData viewData = new ViewData(new int[2][2], 0, 0, new int[2][2], 0, 0, null);
        
        assertNull(viewData.getHoldBrickData(), "Should return null when no brick is held");
    }

    @Test
    void getNextBrickData_shouldReturnCopy() {
        int[][] nextData = {{1, 2}, {3, 4}};
        ViewData viewData = new ViewData(new int[2][2], 0, 0, nextData, 0, 0, null);
        
        int[][] retrieved = viewData.getNextBrickData();
        retrieved[0][0] = 99;
        
        int[][] retrievedAgain = viewData.getNextBrickData();
        assertEquals(1, retrievedAgain[0][0], "Should return a defensive copy");
    }

    @Test
    void ghostPositions_shouldMatchProvidedValues() {
        ViewData viewData = new ViewData(new int[2][2], 3, 5, null, 3, 15, null);
        
        assertEquals(3, viewData.getGhostX());
        assertEquals(15, viewData.getGhostY());
    }
}
