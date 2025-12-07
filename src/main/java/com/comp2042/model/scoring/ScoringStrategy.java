package com.comp2042.model.scoring;

/**
 * Strategy interface for calculating scores in the game.
 * Allows different scoring algorithms to be plugged in without changing Score class.
 * 
 * This demonstrates the Strategy design pattern.
 */
public interface ScoringStrategy {
    
    /**
     * Calculates the score for clearing lines.
     * 
     * @param linesCleared The number of lines cleared
     * @param currentCombo The current combo count (0 if no combo)
     * @return The total score to award
     */
    int calculateLineClearScore(int linesCleared, int currentCombo);
    
    /**
     * Calculates the bonus score for a soft drop (moving piece down manually).
     * 
     * @return The score per row dropped
     */
    int calculateSoftDropScore();
    
    /**
     * Calculates the bonus score for a hard drop (instant drop).
     * 
     * @return The score per row dropped
     */
    int calculateHardDropScore();
}
