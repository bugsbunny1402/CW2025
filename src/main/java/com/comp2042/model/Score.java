package com.comp2042.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private int comboCount = 0;

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
     */
    public int getComboBonus(int baseScore) {
        if (comboCount > 0) {
            return baseScore * comboCount;
        }
        return 0;
    }
}
