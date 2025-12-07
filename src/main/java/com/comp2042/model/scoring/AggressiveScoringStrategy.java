package com.comp2042.model.scoring;

/**
 * High-reward scoring strategy designed for competitive play.
 * Provides significantly higher points than standard scoring, with emphasis
 * on combo chains and decisive play. Suitable for arcade-style modes or
 * experienced players seeking higher scores.
 * 
 * <p>Scoring rules:
 * <ul>
 *   <li>Base score: 100 × (lines cleared)² - double the standard rate</li>
 *   <li>Combo bonus: base score × (combo count + 1) - heavily rewards chains</li>
 *   <li>Soft drop: 2 points per row - encourages faster play</li>
 *   <li>Hard drop: 5 points per row - substantial reward for quick decisions</li>
 * </ul>
 * 
 * <p>This strategy makes combo maintenance crucial for maximizing scores,
 * rewarding players who can consistently clear lines without interruption.
 * 
 * @see ScoringStrategy
 */
public class AggressiveScoringStrategy implements ScoringStrategy {
    
    private static final int BASE_LINE_SCORE = 100;
    private static final int SOFT_DROP_POINTS = 2;
    private static final int HARD_DROP_POINTS = 5;
    
    /**
     * Calculates high-reward scores for line clears with enhanced combo bonuses.
     * Awards 100 × (lines)² base points with multiplicative combo bonuses.
     * 
     * @param linesCleared number of lines cleared simultaneously
     * @param currentCombo active combo count from consecutive clears
     * @return total points with aggressive combo multipliers applied
     */
    @Override
    public int calculateLineClearScore(int linesCleared, int currentCombo) {
        if (linesCleared <= 0) {
            return 0;
        }
        
        // Higher base score
        int baseScore = BASE_LINE_SCORE * linesCleared * linesCleared;
        
        // More aggressive combo bonus
        if (currentCombo > 0) {
            int comboBonus = baseScore * (currentCombo + 1);
            return baseScore + comboBonus;
        }
        
        return baseScore;
    }
    
    /**
     * Returns points awarded per row for soft drops.
     * 
     * @return 2 points per row
     */
    @Override
    public int calculateSoftDropScore() {
        return SOFT_DROP_POINTS;
    }
    
    /**
     * Returns points awarded per row for hard drops.
     * 
     * @return 5 points per row
     */
    @Override
    public int calculateHardDropScore() {
        return HARD_DROP_POINTS;
    }
}
