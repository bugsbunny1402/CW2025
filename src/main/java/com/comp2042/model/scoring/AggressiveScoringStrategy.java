package com.comp2042.model.scoring;

/**
 * Aggressive scoring strategy that rewards combos more heavily.
 * 
 * Scoring rules:
 * - Base score: 100 * (lines cleared)²
 * - Combo bonus: base score * (combo count + 1) - rewards combos more!
 * - Soft drop: 2 points per row
 * - Hard drop: 5 points per row
 * 
 * This strategy is designed for advanced players who want higher scores.
 */
public class AggressiveScoringStrategy implements ScoringStrategy {
    
    private static final int BASE_LINE_SCORE = 100;
    private static final int SOFT_DROP_POINTS = 2;
    private static final int HARD_DROP_POINTS = 5;
    
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
    
    @Override
    public int calculateSoftDropScore() {
        return SOFT_DROP_POINTS;
    }
    
    @Override
    public int calculateHardDropScore() {
        return HARD_DROP_POINTS;
    }
}
