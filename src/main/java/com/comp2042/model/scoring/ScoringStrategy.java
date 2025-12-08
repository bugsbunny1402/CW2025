package com.comp2042.model.scoring;

/**
 * Strategy interface for calculating game scores.
 * Implementations define different scoring rules for line clears and drops.
 * This follows the Strategy design pattern, allowing the scoring algorithm
 * to be changed at runtime without modifying the Score class.
 * 
 * <p>Different strategies can reward players differently:
 * <ul>
 *   <li>StandardScoringStrategy - balanced, traditional Tetris scoring</li>
 *   <li>AggressiveScoringStrategy - higher rewards for skilled play</li>
 * </ul>
 * 
 * @see StandardScoringStrategy
 * @see AggressiveScoringStrategy
 */
public interface ScoringStrategy {
    
    /**
     * Calculates points awarded for clearing one or more lines simultaneously.
     * Typically awards more points for clearing multiple lines at once.
     * 
     * @param linesCleared the number of complete lines removed
     * @param currentCombo the active combo count from consecutive clears
     * @return the total score including combo bonuses
     */
    int calculateLineClearScore(int linesCleared, int currentCombo);
    
    /**
     * Returns points awarded per row when player manually drops a piece.
     * 
     * @return score per soft drop row
     */
    int calculateSoftDropScore();
    
    /**
     * Returns points awarded per row when player uses instant hard drop.
     * 
     * @return score per hard drop row
     */
    int calculateHardDropScore();
}
