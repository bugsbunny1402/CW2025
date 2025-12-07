import com.comp2042.util.MatrixOperations;
import com.comp2042.model.ClearRow;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MatrixOperationsTest {

    @Test
    void copy_shouldCreateDeepCopyOfMatrix() {
        int[][] original = new int[][]{
                {1, 2},
                {3, 4}
        };

        int[][] copy = MatrixOperations.copy(original);

        // Different top-level array
        assertNotSame(original, copy);
        // Different row objects
        assertNotSame(original[0], copy[0]);
        assertNotSame(original[1], copy[1]);

        // But same values
        assertArrayEquals(original[0], copy[0]);
        assertArrayEquals(original[1], copy[1]);

        // Changing original should not affect copy
        original[0][0] = 99;
        assertNotEquals(original[0][0], copy[0][0]);
    }

    @Test
    void checkRemoving_shouldDetectSingleFullRowAndScore50() {
        // 3x4 matrix:
        // row 0: empty
        // row 1: full -> should be cleared
        // row 2: empty
        int[][] matrix = new int[][]{
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0}
        };

        ClearRow result = MatrixOperations.checkRemoving(matrix);

        assertEquals(1, result.getLinesRemoved(), "Expected exactly one cleared row");
        assertEquals(50, result.getScoreBonus(), "Score bonus should be 50 for 1 line (50 * 1^2)");
        assertNotNull(result.getNewMatrix(), "New matrix should not be null");
    }

    @Test
    void checkRemoving_twoFullRowsShouldGive200Bonus() {
        // 4x4 matrix:
        // row 0: full
        // row 1: full
        // row 2: empty
        // row 3: empty
        int[][] matrix = new int[][]{
                {1, 1, 1, 1},
                {2, 2, 2, 2},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        ClearRow result = MatrixOperations.checkRemoving(matrix);

        assertEquals(2, result.getLinesRemoved(), "Expected two cleared rows");
        // scoring rule: 50 * clearedRows^2 = 50 * 4 = 200
        assertEquals(200, result.getScoreBonus(), "Score bonus should be 200 for 2 lines");
    }

    @Test
    void deepCopyList_shouldCreateDeepCopiesOfAllMatrices() {
        int[][] m1 = new int[][]{{1, 0}, {0, 1}};
        int[][] m2 = new int[][]{{2, 2}, {2, 2}};

        List<int[][]> list = new ArrayList<>();
        list.add(m1);
        list.add(m2);

        List<int[][]> copyList = MatrixOperations.deepCopyList(list);

        assertEquals(list.size(), copyList.size());

        // Ensure each matrix is a different object but with same contents
        for (int i = 0; i < list.size(); i++) {
            int[][] original = list.get(i);
            int[][] copy = copyList.get(i);

            assertNotSame(original, copy);
            for (int r = 0; r < original.length; r++) {
                assertArrayEquals(original[r], copy[r]);
            }
        }

        // Modify original and ensure copied list does not change
        list.get(0)[0][0] = 99;
        assertNotEquals(list.get(0)[0][0], copyList.get(0)[0][0]);
    }
}
