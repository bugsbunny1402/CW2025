package com.comp2042.model.scoring;

/**
 * Standard Tetris-style scoring strategy.
 * 
 * Scoring rules:
 * - Base score: 50 * (lines cleared)²
 * - Combo bonus: base score * combo count
 * - Soft drop: 1 point per row
 * - Hard drop: 2 points per row
 * 
 * This is the default scoring system used in the game.
 */
public class StandardScoringStrategy implements ScoringStrategy {
    
    private static final int BASE_LINE_SCORE = 50;
    private static final int SOFT_DROP_POINTS = 1;
    private static final int HARD_DROP_POINTS = 2;
    
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
    
    @Override
    public int calculateSoftDropScore() {
        return SOFT_DROP_POINTS;
    }
    
    @Override
    public int calculateHardDropScore() {
        return HARD_DROP_POINTS;
    }
}
