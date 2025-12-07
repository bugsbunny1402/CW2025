package com.comp2042.model;

import com.comp2042.model.scoring.ScoringStrategy;
import com.comp2042.model.scoring.StandardScoringStrategy;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Tracks and manages the player's score throughout a game session.
 * Handles score accumulation, combo tracking, and integrates with a configurable
 * scoring strategy for flexible point calculations.
 * 
 * <p>The score value is bound to a JavaFX property, allowing the UI to
 * automatically reflect changes without manual updates. Combo tracking
 * encourages consecutive line clears by applying score multipliers.
 * 
 * <p>Scoring features:
 * <ul>
 *   <li>Base scoring for lines cleared, soft drops, and hard drops</li>
 *   <li>Combo system that multiplies points for consecutive clears</li>
 *   <li>Strategy pattern support for different scoring algorithms</li>
 *   <li>Automatic UI binding through JavaFX properties</li>
 * </ul>
 * 
 * @see ScoringStrategy
 * @see StandardScoringStrategy
 */
public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private int comboCount = 0;
    private ScoringStrategy strategy;

    /**
     * Creates a Score instance using the default standard scoring strategy.
     * The score starts at zero with no active combo.
     */
    public Score() {
        this.strategy = new StandardScoringStrategy();
    }

    /**
     * Creates a Score instance with a specified scoring strategy.
     * This allows for alternative scoring rules to be applied.
     * 
     * @param strategy the scoring strategy to use for point calculations
     */
    public Score(ScoringStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Changes the scoring strategy used for future point calculations.
     * The current score value is preserved when switching strategies.
     * 
     * @param strategy the new scoring strategy to apply
     */
    public void setStrategy(ScoringStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Returns the JavaFX property containing the current score value.
     * This property can be bound to UI elements for automatic updates.
     * 
     * @return the observable score property
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Adds the specified number of points to the current score.
     * The UI automatically updates due to property binding.
     * 
     * @param i the number of points to add
     */
    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    /**
     * Resets the score to zero and clears any active combo.
     * Typically called when starting a new game.
     */
    public void reset() {
        score.setValue(0);
        comboCount = 0;
    }
    
    /**
     * Increments the combo counter when lines are successfully cleared.
     * Each consecutive clear increases the multiplier for bonus points.
     */
    public void incrementCombo() {
        comboCount++;
    }
    
    /**
     * Resets the combo counter to zero when no lines are cleared.
     * This breaks the combo chain and returns the multiplier to normal.
     */
    public void resetCombo() {
        comboCount = 0;
    }
    
    /**
     * Returns the current combo count representing consecutive line clears.
     * A value of 0 indicates no active combo.
     * 
     * @return the number of consecutive line clears
     */
    public int getComboCount() {
        return comboCount;
    }
    
    /**
     * Calculates the score multiplier based on the current combo.
     * Starts at x1 for no combo, x2 for first combo, x3 for second, etc.
     * 
     * @return the current combo multiplier value
     */
    public int getComboMultiplier() {
        return comboCount > 0 ? comboCount + 1 : 1;
    }
    
    /**
     * Calculates additional bonus points based on the active combo.
     * The bonus equals the base score multiplied by the combo count.
     * 
     * @param baseScore the base score before combo multiplication
     * @return the bonus points earned from the combo, or 0 if no combo
     */
    public int getComboBonus(int baseScore) {
        if (comboCount > 0) {
            return baseScore * comboCount;
        }
        return 0;
    }
    
    /**
     * Calculates the total score for clearing lines using the current strategy.
     * Includes both base points and combo bonuses.
     * 
     * @param linesCleared the number of lines cleared simultaneously
     * @return the total score awarded
     */
    public int calculateLineClearScore(int linesCleared) {
        return strategy.calculateLineClearScore(linesCleared, comboCount);
    }
    
    /**
     * Returns the points awarded per row for soft drop movements.
     * Soft drops occur when the player manually moves the piece down.
     * 
     * @return points per soft drop row
     */
    public int getSoftDropBonus() {
        return strategy.calculateSoftDropScore();
    }
    
    /**
     * Returns the points awarded per row for hard drop movements.
     * Hard drops instantly place the piece at the bottom.
     * 
     * @return points per hard drop row
     */
    public int getHardDropBonus() {
        return strategy.calculateHardDropScore();
    }
}
