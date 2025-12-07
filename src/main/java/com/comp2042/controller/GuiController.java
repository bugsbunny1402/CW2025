package com.comp2042.controller;

import com.comp2042.events.*;
import com.comp2042.model.*;
import com.comp2042.ui.*;
import com.comp2042.util.HighScoreManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main GUI controller for the game view.
 * Handles user input, rendering, and UI updates.
 */
public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;
    
    // Animation manager for visual effects
    private final AnimationManager animationManager = new AnimationManager();

    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private GridPane ghostPanel; // Added Ghost Panel

    // Preview Panels
    @FXML private GridPane nextBrickPanel;
    @FXML private GridPane holdBrickPanel; // Added Hold Panel

    @FXML private GameOverPanel gameOverPanel;
    @FXML private StackPane pauseMenu; // Added Pause Overlay

    // Labels
    @FXML private Label scoreLabel;
    @FXML private Label levelLabel;
    @FXML private Label highScoreLabel;
    @FXML private Label comboLabel;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;
    private Rectangle[][] ghostRectangles; // Added Ghost Display
    private Rectangle[][] nextBrickDisplay;
    private Rectangle[][] holdBrickDisplay; // Added Hold Display

    private InputEventListener eventListener;
    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Font loading removed (Using system fonts for neon look)
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.P) {
                    togglePause();
                    keyEvent.consume();
                    return;
                }

                if (!isPause.get() && !isGameOver.get()) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        DownData downData = eventListener.onHardDropEvent(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
                        updateViews(downData.getViewData());
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.C) {
                        ViewData viewData = eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER));
                        updateViews(viewData);
                        keyEvent.consume();
                    }
                }

                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }
            }
        });

        gameOverPanel.setVisible(false);
        if (pauseMenu != null) pauseMenu.setVisible(false);

        // Reflection Effect for the floor
        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);

        // Load High Score
        int best = HighScoreManager.loadHighScore();
        if (highScoreLabel != null) {
            highScoreLabel.setText(String.valueOf(best));
        }
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }

        // Initialize Ghost Panel (if available)
        if (ghostPanel != null) {
            ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    rectangle.setFill(Color.TRANSPARENT);
                    ghostRectangles[i][j] = rectangle;
                    ghostPanel.add(rectangle, j, i);
                }
            }
        }

        // Initial positioning
        updateBrickPanelPosition(brick);
        if (ghostPanel != null) {
            updateGhostPanelPosition(brick);
        }

        // Initial Previews
        refreshNextBrick(brick.getNextBrickData());
        refreshHoldBrick(brick.getHoldBrickData());

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    // NEON COLOR PALETTE
    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0: returnPaint = Color.TRANSPARENT; break;
            case 1: returnPaint = Color.web("#00FFFF"); break; // Cyan
            case 2: returnPaint = Color.web("#B026FF"); break; // Purple
            case 3: returnPaint = Color.web("#39FF14"); break; // Green
            case 4: returnPaint = Color.web("#FFFF00"); break; // Yellow
            case 5: returnPaint = Color.web("#FF3131"); break; // Red
            case 6: returnPaint = Color.web("#1F51FF"); break; // Blue
            case 7: returnPaint = Color.web("#FF10F0"); break; // Pink
            default: returnPaint = Color.WHITE; break;
        }
        return returnPaint;
    }

    private void refreshBrick(ViewData brick) {
        if (!isPause.get()) {
            updateBrickPanelPosition(brick);
            if (ghostPanel != null && ghostRectangles != null) {
                updateGhostPanelPosition(brick);
            }
            
            int[][] currentBrickData = brick.getBrickData();
            
            // Clear and update current brick rectangles
            for (int i = 0; i < rectangles.length; i++) {
                for (int j = 0; j < rectangles[i].length; j++) {
                    setRectangleData(0, rectangles[i][j]);
                }
            }
            for (int i = 0; i < currentBrickData.length; i++) {
                for (int j = 0; j < currentBrickData[i].length; j++) {
                    setRectangleData(currentBrickData[i][j], rectangles[i][j]);
                }
            }
            
            // Clear and update ghost rectangles with semi-transparent version (if available)
            if (ghostPanel != null && ghostRectangles != null) {
                for (int i = 0; i < ghostRectangles.length; i++) {
                    for (int j = 0; j < ghostRectangles[i].length; j++) {
                        setGhostRectangleData(0, ghostRectangles[i][j]);
                    }
                }
                for (int i = 0; i < currentBrickData.length; i++) {
                    for (int j = 0; j < currentBrickData[i].length; j++) {
                        setGhostRectangleData(currentBrickData[i][j], ghostRectangles[i][j]);
                    }
                }
            }
        }
    }
    /**
     * Updates the layout position of the falling brick panel.
     * Adjusted for StackPane alignment (bricks move relative to the game board).
     */
    private void updateBrickPanelPosition(ViewData brick) {
        // Base X must match the FXML layoutX of the gameBoard (155) + border width (3)
        double baseX = 155.0 + 3.0;
        // Base Y must match the FXML layoutY of the gameBoard (30) + border width (3)
        double baseY = 30.0 + 3.0;

        // Calculate offsets based on the grid coordinates (Cell Size 20 + 1px Gap)
        double xOffset = brick.getxPosition() * (BRICK_SIZE + 1);
        double yOffset = (brick.getyPosition() - 2) * (BRICK_SIZE + 1); // -2 to account for hidden rows

        brickPanel.setLayoutX(baseX + xOffset);
        brickPanel.setLayoutY(baseY + yOffset);
    }

    /**
     * Updates the layout position of the ghost panel (shows where the brick will land).
     */
    private void updateGhostPanelPosition(ViewData brick) {
        if (ghostPanel == null) return;
        
        // Base X must match the FXML layoutX of the gameBoard (155) + border width (3)
        double baseX = 155.0 + 3.0;
        // Base Y must match the FXML layoutY of the gameBoard (30) + border width (3)
        double baseY = 30.0 + 3.0;

        // Calculate offsets based on the ghost coordinates (Cell Size 20 + 1px Gap)
        double xOffset = brick.getGhostX() * (BRICK_SIZE + 1);
        double yOffset = (brick.getGhostY() - 2) * (BRICK_SIZE + 1); // -2 to account for hidden rows

        ghostPanel.setLayoutX(baseX + xOffset);
        ghostPanel.setLayoutY(baseY + yOffset);
    }

    // Generic helper to update all views at once
    private void updateViews(ViewData data) {
        refreshBrick(data);
        refreshNextBrick(data.getNextBrickData());
        refreshHoldBrick(data.getHoldBrickData());
    }

    private void refreshNextBrick(int[][] nextBrickData) {
        if (nextBrickPanel == null) return;
        if (nextBrickDisplay == null) {
            nextBrickDisplay = initGrid(nextBrickPanel, 4, 4);
        }
        updateGrid(nextBrickDisplay, nextBrickData);
    }

    private void refreshHoldBrick(int[][] holdData) {
        if (holdBrickPanel == null) return;
        if (holdBrickDisplay == null) {
            holdBrickDisplay = initGrid(holdBrickPanel, 4, 4);
        }
        updateGrid(holdBrickDisplay, holdData);
    }

    private Rectangle[][] initGrid(GridPane panel, int rows, int cols) {
        Rectangle[][] grid = new Rectangle[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Rectangle r = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                r.setFill(Color.TRANSPARENT);
                grid[i][j] = r;
                panel.add(r, j, i);
            }
        }
        return grid;
    }

    private void updateGrid(Rectangle[][] grid, int[][] data) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (data != null && i < data.length && j < data[i].length) {
                    setRectangleData(data[i][j], grid[i][j]);
                } else {
                    setRectangleData(0, grid[i][j]);
                }
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    // NEON GLOW EFFECT
    private void setRectangleData(int color, Rectangle rectangle) {
        Paint fill = getFillColor(color);
        rectangle.setFill(fill);
        rectangle.setArcHeight(5);
        rectangle.setArcWidth(5);

        if (color == 0) {
            rectangle.setEffect(null);
        } else {
            DropShadow glow = new DropShadow();
            glow.setColor(((Color) fill).brighter());
            glow.setRadius(15);
            glow.setSpread(0.25);
            rectangle.setEffect(glow);
        }
    }

    // GHOST PIECE STYLING (semi-transparent with subtle glow)
    private void setGhostRectangleData(int color, Rectangle rectangle) {
        if (color == 0) {
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setEffect(null);
        } else {
            Paint fill = getFillColor(color);
            // Make it semi-transparent (30% opacity)
            Color ghostColor = ((Color) fill).deriveColor(0, 1, 1, 0.3);
            rectangle.setFill(ghostColor);
            rectangle.setArcHeight(5);
            rectangle.setArcWidth(5);
            
            // Add subtle glow effect
            DropShadow glow = new DropShadow();
            glow.setColor(((Color) fill).deriveColor(0, 1, 1, 0.5));
            glow.setRadius(10);
            glow.setSpread(0.15);
            rectangle.setEffect(glow);
        }
    }

    private void moveDown(MoveEvent event) {
        if (!isPause.get()) {
            DownData downData = eventListener.onDownEvent(event);
            updateViews(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty scoreProperty) {
        scoreLabel.textProperty().bind(scoreProperty.asString());
    }

    public void setLevel(int level) {
        if (levelLabel != null) {
            levelLabel.setText(String.valueOf(level));
        }
    }

    public void setCombo(int combo) {
        if (comboLabel != null) {
            if (combo > 0) {
                comboLabel.setText("x" + (combo + 1));
            } else {
                comboLabel.setText("");
            }
        }
    }

    public void showScoreNotification(int score, boolean isCombo) {
        if (groupNotification != null) {
            String text = isCombo ? "COMBO!\n+" + score : "+" + score;
            NotificationPanel notificationPanel = new NotificationPanel(text, isCombo);
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
        }
    }

    public void setGameSpeed(long speedMillis) {
        if (timeLine != null) {
            timeLine.stop();
        }
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(speedMillis),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        if (!isPause.get() && !isGameOver.get()) {
            timeLine.play();
        }
    }

    public void gameOver() {
        if (timeLine != null) timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.set(true);
        if (pauseMenu != null) pauseMenu.setVisible(false);

        // Update High Score
        try {
            int currentScore = Integer.parseInt(scoreLabel.getText());
            int currentBest = HighScoreManager.loadHighScore();
            if (currentScore > currentBest) {
                HighScoreManager.saveHighScore(currentScore);
                if (highScoreLabel != null) highScoreLabel.setText(String.valueOf(currentScore));
            }
        } catch (NumberFormatException ignored) {}
    }

    public void newGame(ActionEvent actionEvent) {
        if (timeLine != null) timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();

        isPause.set(false);
        isGameOver.set(false);
        if (pauseMenu != null) pauseMenu.setVisible(false);

        // Reset Views
        refreshHoldBrick(null);
        
        // IMPORTANT: Restart the game timeline!
        if (timeLine != null) {
            timeLine.play();
        }
    }

    private void togglePause() {
        if (isGameOver.get()) return;

        if (isPause.get()) {
            if (timeLine != null) timeLine.play();
            isPause.set(false);
            if (pauseMenu != null) pauseMenu.setVisible(false);
        } else {
            if (timeLine != null) timeLine.pause();
            isPause.set(true);
            if (pauseMenu != null) pauseMenu.setVisible(true);
        }
        gamePanel.requestFocus();
    }

    public void pauseGame(ActionEvent actionEvent) {
        togglePause();
    }

    public void resumeGame(ActionEvent actionEvent) {
        if (isPause.get()) {
            if (timeLine != null) timeLine.play();
            isPause.set(false);
            if (pauseMenu != null) pauseMenu.setVisible(false);
            gamePanel.requestFocus();
        }
    }

    /**
     * Delegates line clear animation to AnimationManager.
     * Keeps GuiController focused on input/output rather than animation details.
     */
    public void animateLineClear(java.util.List<Integer> rowIndices, Runnable callback) {
        animationManager.animateLineClear(displayMatrix, rowIndices, callback);
    }
}
