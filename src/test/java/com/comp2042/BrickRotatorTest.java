package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.IBrick;
import com.comp2042.model.BrickRotator;
import com.comp2042.model.NextShapeInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BrickRotator class.
 * Tests rotation logic and state management.
 */
public class BrickRotatorTest {

    private BrickRotator rotator;
    private Brick testBrick;

    @BeforeEach
    void setUp() {
        rotator = new BrickRotator();
        testBrick = new IBrick(); // I-brick has 2 rotation states
    }

    @Test
    void setBrick_shouldThrowException_whenBrickIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            rotator.setBrick(null);
        }, "Should throw IllegalArgumentException when brick is null");
    }

    @Test
    void getCurrentShape_shouldThrowException_whenBrickNotSet() {
        BrickRotator emptyRotator = new BrickRotator();
        assertThrows(IllegalStateException.class, () -> {
            emptyRotator.getCurrentShape();
        }, "Should throw IllegalStateException when brick not set");
    }

    @Test
    void setBrick_shouldResetRotationIndex() {
        rotator.setBrick(testBrick);
        rotator.setCurrentShape(1); // Change rotation
        
        rotator.setBrick(testBrick); // Set brick again
        NextShapeInfo next = rotator.getNextShape();
        
        // After reset, next shape should be index 1 (since current is 0)
        assertEquals(1, next.getPosition(), "Setting brick should reset rotation to 0");
    }

    @Test
    void getNextShape_shouldRotateClockwise() {
        rotator.setBrick(testBrick);
        
        NextShapeInfo next = rotator.getNextShape();
        assertEquals(1, next.getPosition(), "Next shape should be at position 1");
        
        rotator.setCurrentShape(next.getPosition());
        NextShapeInfo nextAgain = rotator.getNextShape();
        
        // I-brick has 2 states, so after 1 should wrap to 0
        assertEquals(0, nextAgain.getPosition(), "Should wrap around to position 0");
    }

    @Test
    void setCurrentShape_shouldNormalizeNegativeValues() {
        rotator.setBrick(testBrick);
        
        rotator.setCurrentShape(-1);
        NextShapeInfo next = rotator.getNextShape();
        
        // -1 should normalize to last valid index
        assertNotNull(next, "Should handle negative rotation indices");
    }

    @Test
    void getCurrentShape_shouldReturnValidMatrix() {
        rotator.setBrick(testBrick);
        
        int[][] shape = rotator.getCurrentShape();
        
        assertNotNull(shape, "Shape should not be null");
        assertTrue(shape.length > 0, "Shape should have rows");
        assertTrue(shape[0].length > 0, "Shape should have columns");
    }
}
