package com.comp2042;

import com.comp2042.model.Score;
import com.comp2042.model.scoring.AggressiveScoringStrategy;
import com.comp2042.model.scoring.ScoringStrategy;
import com.comp2042.model.scoring.StandardScoringStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Scoring Strategy pattern.
 * Demonstrates that different strategies produce different scores.
 */
public class ScoringStrategyTest {

    @Test
    void standardStrategy_shouldCalculateCorrectLineScore() {
        ScoringStrategy strategy = new StandardScoringStrategy();
        
        // 1 line: 50 * 1² = 50
        assertEquals(50, strategy.calculateLineClearScore(1, 0));
        
        // 2 lines: 50 * 2² = 200
        assertEquals(200, strategy.calculateLineClearScore(2, 0));
        
        // 4 lines: 50 * 4² = 800
        assertEquals(800, strategy.calculateLineClearScore(4, 0));
    }

    @Test
    void standardStrategy_shouldCalculateComboBonus() {
        ScoringStrategy strategy = new StandardScoringStrategy();
        
        // 1 line with combo 1: base 50 + (50 * 1) = 100
        assertEquals(100, strategy.calculateLineClearScore(1, 1));
        
        // 2 lines with combo 2: base 200 + (200 * 2) = 600
        assertEquals(600, strategy.calculateLineClearScore(2, 2));
    }

    @Test
    void aggressiveStrategy_shouldGiveHigherScores() {
        ScoringStrategy standard = new StandardScoringStrategy();
        ScoringStrategy aggressive = new AggressiveScoringStrategy();
        
        // Same input, different output
        int standardScore = standard.calculateLineClearScore(2, 0);
        int aggressiveScore = aggressive.calculateLineClearScore(2, 0);
        
        assertTrue(aggressiveScore > standardScore, 
            "Aggressive strategy should give higher scores");
    }

    @Test
    void aggressiveStrategy_shouldHaveBetterComboBonus() {
        ScoringStrategy standard = new StandardScoringStrategy();
        ScoringStrategy aggressive = new AggressiveScoringStrategy();
        
        // With combo, aggressive should be significantly higher
        int standardCombo = standard.calculateLineClearScore(2, 3);
        int aggressiveCombo = aggressive.calculateLineClearScore(2, 3);
        
        assertTrue(aggressiveCombo > standardCombo, 
            "Aggressive strategy should have better combo bonuses");
    }

    @Test
    void score_canSwitchStrategies() {
        Score score = new Score(); // Starts with standard
        
        score.add(100);
        assertEquals(100, score.scoreProperty().get());
        
        // Switch to aggressive strategy mid-game
        score.setStrategy(new AggressiveScoringStrategy());
        
        // Strategy changed, but score persists
        assertEquals(100, score.scoreProperty().get());
    }

    @Test
    void score_usesStrategyForCalculations() {
        Score score = new Score(new StandardScoringStrategy());
        
        int softDrop = score.getSoftDropBonus();
        int hardDrop = score.getHardDropBonus();
        
        assertEquals(1, softDrop, "Standard strategy: soft drop = 1");
        assertEquals(2, hardDrop, "Standard strategy: hard drop = 2");
        
        // Switch to aggressive
        score.setStrategy(new AggressiveScoringStrategy());
        
        softDrop = score.getSoftDropBonus();
        hardDrop = score.getHardDropBonus();
        
        assertEquals(2, softDrop, "Aggressive strategy: soft drop = 2");
        assertEquals(5, hardDrop, "Aggressive strategy: hard drop = 5");
    }

    @Test
    void standardStrategy_dropScores() {
        ScoringStrategy strategy = new StandardScoringStrategy();
        
        assertEquals(1, strategy.calculateSoftDropScore());
        assertEquals(2, strategy.calculateHardDropScore());
    }

    @Test
    void aggressiveStrategy_dropScores() {
        ScoringStrategy strategy = new AggressiveScoringStrategy();
        
        assertEquals(2, strategy.calculateSoftDropScore());
        assertEquals(5, strategy.calculateHardDropScore());
    }

    @Test
    void zeroLines_shouldReturnZeroScore() {
        ScoringStrategy strategy = new StandardScoringStrategy();
        
        assertEquals(0, strategy.calculateLineClearScore(0, 0));
        assertEquals(0, strategy.calculateLineClearScore(0, 5));
    }

    @Test
    void negativeLines_shouldReturnZeroScore() {
        ScoringStrategy strategy = new StandardScoringStrategy();
        
        assertEquals(0, strategy.calculateLineClearScore(-1, 0));
    }
}
