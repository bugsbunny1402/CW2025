package com.comp2042.model.scoring;

/**
 * Standard Tetris scoring implementation providing balanced point rewards.
 * Uses the classic formula where clearing multiple lines simultaneously
 * yields exponentially more points to encourage strategic play.
 * 
 * <p>Scoring rules:
 * <ul>
 *   <li>Base score: 50 × (lines cleared)² - rewards multi-line clears</li>
 *   <li>Combo bonus: base score × combo count - rewards consecutive clears</li>
 *   <li>Soft drop: 1 point per row - small reward for manual placement</li>
 *   <li>Hard drop: 2 points per row - encourages decisive play</li>
 * </ul>
 * 
 * <p>Examples:
 * <ul>
 *   <li>Single line: 50 points</li>
 *   <li>Double line: 200 points (4x more than single)</li>
 *   <li>Triple line: 450 points</li>
 *   <li>Tetris (4 lines): 800 points</li>
 * </ul>
 * 
 * @see ScoringStrategy
 */
public class StandardScoringStrategy implements ScoringStrategy {
    
    private static final int BASE_LINE_SCORE = 50;
    private static final int SOFT_DROP_POINTS = 1;
    private static final int HARD_DROP_POINTS = 2;
    
    /**
     * Calculates score for line clears using the standard formula.
     * Awards 50 × (lines)² base points plus combo bonuses for consecutive clears.
     * 
     * @param linesCleared number of lines cleared simultaneously (1-4 typical)
     * @param currentCombo active combo count from previous consecutive clears
     * @return total points awarded including combo bonuses
     */
    @Override
    public int calculateLineClearScore(int linesCleared, int currentCombo) {
        if (linesCleared <= 0) {
            return 0;
        }
        
        // Base score: 50 * lines²
        int baseScore = BASE_LINE_SCORE * linesCleared * linesCleared;
        
        // Combo bonus: multiply base score by combo count
        if (currentCombo > 0) {
            int comboBonus = baseScore * currentCombo;
            return baseScore + comboBonus;
        }
        
        return baseScore;
    }
    
    /**
     * Returns points awarded per row for soft drops.
     * 
     * @return 1 point per row
     */
    @Override
    public int calculateSoftDropScore() {
        return SOFT_DROP_POINTS;
    }
    
    /**
     * Returns points awarded per row for hard drops.
     * 
     * @return 2 points per row
     */
    @Override
    public int calculateHardDropScore() {
        return HARD_DROP_POINTS;
    }
}
