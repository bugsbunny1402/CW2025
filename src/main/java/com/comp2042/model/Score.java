package com.comp2042.model;

import com.comp2042.model.scoring.ScoringStrategy;
import com.comp2042.model.scoring.StandardScoringStrategy;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Manages the player's score using a configurable scoring strategy.
 * Demonstrates the Strategy design pattern for flexible scoring algorithms.
 */
public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private int comboCount = 0;
    private ScoringStrategy strategy;

    /**
     * Creates a Score object with the standard scoring strategy.
     */
    public Score() {
        this.strategy = new StandardScoringStrategy();
    }

    /**
     * Creates a Score object with a custom scoring strategy.
     * 
     * @param strategy The scoring strategy to use
     */
    public Score(ScoringStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Sets a new scoring strategy (allows changing mid-game if desired).
     * 
     * @param strategy The new scoring strategy
     */
    public void setStrategy(ScoringStrategy strategy) {
        this.strategy = strategy;
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    public void reset() {
        score.setValue(0);
        comboCount = 0;
    }
    
    /**
     * Increment combo counter when lines are cleared
     */
    public void incrementCombo() {
        comboCount++;
    }
    
    /**
     * Reset combo when no lines are cleared
     */
    public void resetCombo() {
        comboCount = 0;
    }
    
    /**
     * Get current combo count
     */
    public int getComboCount() {
        return comboCount;
    }
    
    /**
     * Get combo multiplier (starts at x2 for first combo)
     */
    public int getComboMultiplier() {
        return comboCount > 0 ? comboCount + 1 : 1;
    }
    
    /**
     * Calculate bonus score based on combo
     * Delegates to the current scoring strategy.
     */
    public int getComboBonus(int baseScore) {
        if (comboCount > 0) {
            return baseScore * comboCount;
        }
        return 0;
    }
    
    /**
     * Calculates score for clearing lines using the current strategy.
     * 
     * @param linesCleared Number of lines cleared
     * @return The score to award
     */
    public int calculateLineClearScore(int linesCleared) {
        return strategy.calculateLineClearScore(linesCleared, comboCount);
    }
    
    /**
     * Gets the soft drop bonus points using the current strategy.
     * 
     * @return Points per soft drop row
     */
    public int getSoftDropBonus() {
        return strategy.calculateSoftDropScore();
    }
    
    /**
     * Gets the hard drop bonus points using the current strategy.
     * 
     * @return Points per hard drop row
     */
    public int getHardDropBonus() {
        return strategy.calculateHardDropScore();
    }
}
