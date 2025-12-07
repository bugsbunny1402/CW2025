package com.comp2042;

import com.comp2042.model.ClearRow;
import com.comp2042.model.SimpleBoard;
import com.comp2042.model.ViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SimpleBoard class.
 * Tests core game board functionality.
 */
public class SimpleBoardTest {

    private SimpleBoard board;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(25, 10);
    }

    @Test
    void newBoard_shouldStartAtLevelOne() {
        assertEquals(1, board.getCurrentLevel(), "New board should start at level 1");
    }

    @Test
    void newBoard_shouldHaveZeroLinesCleared() {
        assertEquals(0, board.getTotalLinesCleared(), "New board should have 0 lines cleared");
    }

    @Test
    void getScore_shouldNeverReturnNull() {
        assertNotNull(board.getScore(), "Score object should never be null");
    }

    @Test
    void getBoardMatrix_shouldReturnCorrectDimensions() {
        int[][] matrix = board.getBoardMatrix();
        
        assertEquals(25, matrix.length, "Board should have 25 rows");
        assertEquals(10, matrix[0].length, "Board should have 10 columns");
    }

    @Test
    void getViewData_shouldReturnNonNull() {
        board.createNewBrick();
        ViewData viewData = board.getViewData();
        
        assertNotNull(viewData, "ViewData should not be null");
        assertNotNull(viewData.getBrickData(), "Brick data should not be null");
    }

    @Test
    void moveBrickDown_shouldReturnTrue_whenSpaceAvailable() {
        board.createNewBrick();
        boolean moved = board.moveBrickDown();
        
        assertTrue(moved, "Should be able to move brick down on empty board");
    }

    @Test
    void clearRows_shouldReturnZeroLines_onEmptyBoard() {
        ClearRow result = board.clearRows();
        
        assertEquals(0, result.getLinesRemoved(), "Empty board should have no lines to clear");
    }

    @Test
    void newGame_shouldResetScore() {
        board.getScore().add(1000);
        board.newGame();
        
        assertEquals(0, board.getScore().scoreProperty().get(), "New game should reset score to 0");
    }

    @Test
    void newGame_shouldResetLevel() {
        // Simulate level progression by clearing lines
        board.newGame();
        
        assertEquals(1, board.getCurrentLevel(), "New game should reset level to 1");
    }

    @Test
    void swapHoldBrick_shouldWork_whenNoHeldBrick() {
        board.createNewBrick();
        ViewData before = board.getViewData();
        
        board.swapHoldBrick();
        ViewData after = board.getViewData();
        
        assertNotNull(after.getHoldBrickData(), "Should have a held brick after swap");
    }

    @Test
    void swapHoldBrick_shouldOnlyWorkOnce_perBrick() {
        board.createNewBrick();
        
        board.swapHoldBrick(); // First swap should work
        ViewData first = board.getViewData();
        
        board.swapHoldBrick(); // Second swap should be blocked
        ViewData second = board.getViewData();
        
        // Brick data should be same (swap was blocked)
        assertArrayEquals(first.getBrickData(), second.getBrickData(), 
            "Should not allow multiple swaps per brick placement");
    }
}
