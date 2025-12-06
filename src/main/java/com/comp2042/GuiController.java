package com.comp2042;

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
import java.util.List;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    private GridPane ghostPanel; // Ghost piece panel (created programmatically)

    // Preview Panels
    @FXML private GridPane nextBrickPanel;
    @FXML private GridPane holdBrickPanel;

    @FXML private GameOverPanel gameOverPanel;
    @FXML private StackPane pauseMenu; 

    // Labels
    @FXML private Label scoreLabel;
    @FXML private Label levelLabel;
    @FXML private Label highScoreLabel;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;
    private Rectangle[][] ghostRectangles; // Ghost piece rectangles
    private Rectangle[][] nextBrickDisplay;
    private Rectangle[][] holdBrickDisplay; // Added Hold Display

    private InputEventListener eventListener;
    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Create ghostPanel programmatically
        ghostPanel = new GridPane();
        ghostPanel.setHgap(1);
        ghostPanel.setVgap(1);
        ghostPanel.setLayoutX(125.0);
        ghostPanel.setLayoutY(30.0);
        ghostPanel.setMouseTransparent(true);
        
        // Add ghostPanel to the parent pane (behind brickPanel)
        if (brickPanel != null && brickPanel.getParent() instanceof javafx.scene.layout.Pane) {
            javafx.scene.layout.Pane parentPane = (javafx.scene.layout.Pane) brickPanel.getParent();
            int brickPanelIndex = parentPane.getChildren().indexOf(brickPanel);
            parentPane.getChildren().add(brickPanelIndex, ghostPanel);
        }
        
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
                        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                            NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                            groupNotification.getChildren().add(notificationPanel);
                            notificationPanel.showScore(groupNotification.getChildren());
                        }
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

        // Initialize ghost panel
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                ghostRectangles[i][j] = rectangle;
                if (ghostPanel != null) {
                    ghostPanel.add(rectangle, j, i);
                }
            }
        }

        // Initial positioning
        updateBrickPanelPosition(brick);
        updateGhostPanelPosition(brick);
        refreshGhostPiece(brick);

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
            int[][] currentBrickData = brick.getBrickData();
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
            
            // Update ghost piece
            updateGhostPanelPosition(brick);
            refreshGhostPiece(brick);
        }
    }
    /**
     * Updates the layout position of the falling brick panel.
     * Adjusted for StackPane alignment (bricks move relative to the game board).
     */
    private void updateBrickPanelPosition(ViewData brick) {
        // Base X must match the FXML layoutX of the gameBoard (125)
        double baseX = 125.0;
        // Base Y must match the FXML layoutY of the gameBoard (30)
        double baseY = 30.0;

        // Calculate offsets based on the grid coordinates (Cell Size 20 + 1px Gap)
        double xOffset = brick.getxPosition() * (BRICK_SIZE + 1);
        double yOffset = (brick.getyPosition() - 2) * (BRICK_SIZE + 1); // -2 to account for hidden rows

        brickPanel.setLayoutX(baseX + xOffset);
        brickPanel.setLayoutY(baseY + yOffset);
    }

    /**
     * Updates the layout position of the ghost piece panel.
     */
    private void updateGhostPanelPosition(ViewData brick) {
        if (ghostPanel == null) return;
        
        double baseX = 125.0;
        double baseY = 30.0;

        double xOffset = brick.getGhostX() * (BRICK_SIZE + 1);
        double yOffset = (brick.getGhostY() - 2) * (BRICK_SIZE + 1);

        ghostPanel.setLayoutX(baseX + xOffset);
        ghostPanel.setLayoutY(baseY + yOffset);
    }

    /**
     * Refreshes the ghost piece rectangles with semi-transparent appearance.
     * The ghost uses the same color as the piece but with reduced opacity.
     */
    private void refreshGhostPiece(ViewData brick) {
        if (ghostPanel == null || ghostRectangles == null) return;
        
        int[][] currentBrickData = brick.getBrickData();
        for (int i = 0; i < ghostRectangles.length; i++) {
            for (int j = 0; j < ghostRectangles[i].length; j++) {
                if (i < currentBrickData.length && j < currentBrickData[i].length && currentBrickData[i][j] != 0) {
                    // Get the original color of the piece
                    Paint originalColor = getFillColor(currentBrickData[i][j]);
                    
                    // Make it semi-transparent for ghost effect
                    if (originalColor instanceof Color) {
                        Color color = (Color) originalColor;
                        // Use the same color but with 25% opacity for fill and 40% for border
                        ghostRectangles[i][j].setFill(Color.rgb(
                            (int)(color.getRed() * 255),
                            (int)(color.getGreen() * 255),
                            (int)(color.getBlue() * 255),
                            0.25  // 25% opacity
                        ));
                        ghostRectangles[i][j].setStroke(Color.rgb(
                            (int)(color.getRed() * 255),
                            (int)(color.getGreen() * 255),
                            (int)(color.getBlue() * 255),
                            0.4  // 40% opacity for border
                        ));
                        ghostRectangles[i][j].setStrokeWidth(1.5);
                        ghostRectangles[i][j].setArcHeight(5);
                        ghostRectangles[i][j].setArcWidth(5);
                    }
                } else {
                    ghostRectangles[i][j].setFill(Color.TRANSPARENT);
                    ghostRectangles[i][j].setStroke(null);
                }
            }
        }
    }

    /**
     * Animates line clearing with flash effects before removing them.
     * Creates a visually appealing sequence:
     * 1. Flash white 3 times
     * 2. Fade out
     * 3. Update the board
     */
    public void animateLineClear(List<Integer> clearedRowIndices, Runnable onComplete) {
        if (clearedRowIndices == null || clearedRowIndices.isEmpty()) {
            onComplete.run();
            return;
        }
        
        // Store original colors
        final int[][] originalColors = new int[clearedRowIndices.size()][];
        for (int idx = 0; idx < clearedRowIndices.size(); idx++) {
            int rowIndex = clearedRowIndices.get(idx);
            if (rowIndex >= 2 && rowIndex < displayMatrix.length) {
                originalColors[idx] = new int[displayMatrix[rowIndex].length];
                for (int j = 0; j < displayMatrix[rowIndex].length; j++) {
                    // Store color value (1-7)
                    Paint fill = displayMatrix[rowIndex][j].getFill();
                    originalColors[idx][j] = getColorIndex(fill);
                }
            }
        }
        
        // Flash animation: 3 flashes at 100ms intervals
        final int[] flashCount = {0};
        final Timeline flashTimeline = new Timeline();
        
        for (int flash = 0; flash < 6; flash++) { // 3 flashes = 6 toggles (on/off)
            final boolean isWhite = (flash % 2 == 0);
            KeyFrame keyFrame = new KeyFrame(
                Duration.millis(flash * 80),
                e -> {
                    for (int idx = 0; idx < clearedRowIndices.size(); idx++) {
                        int rowIndex = clearedRowIndices.get(idx);
                        if (rowIndex >= 2 && rowIndex < displayMatrix.length) {
                            for (int j = 0; j < displayMatrix[rowIndex].length; j++) {
                                if (isWhite) {
                                    // Flash white
                                    displayMatrix[rowIndex][j].setFill(Color.WHITE);
                                } else {
                                    // Restore original color
                                    setRectangleData(originalColors[idx][j], displayMatrix[rowIndex][j]);
                                }
                            }
                        }
                    }
                }
            );
            flashTimeline.getKeyFrames().add(keyFrame);
        }
        
        // Fade out animation
        KeyFrame fadeKeyFrame = new KeyFrame(
            Duration.millis(480), // After flashing
            e -> {
                for (int idx = 0; idx < clearedRowIndices.size(); idx++) {
                    int rowIndex = clearedRowIndices.get(idx);
                    if (rowIndex >= 2 && rowIndex < displayMatrix.length) {
                        for (int j = 0; j < displayMatrix[rowIndex].length; j++) {
                            // Fade to transparent
                            displayMatrix[rowIndex][j].setFill(Color.rgb(255, 255, 255, 0.3));
                        }
                    }
                }
            }
        );
        flashTimeline.getKeyFrames().add(fadeKeyFrame);
        
        // Final clear - call the completion callback
        KeyFrame clearKeyFrame = new KeyFrame(
            Duration.millis(600), // Total animation duration
            e -> onComplete.run()
        );
        flashTimeline.getKeyFrames().add(clearKeyFrame);
        
        flashTimeline.play();
    }
    
    /**
     * Helper to get color index from Paint for animation
     */
    private int getColorIndex(Paint paint) {
        if (!(paint instanceof Color)) return 0;
        Color c = (Color) paint;
        
        // Match colors to their indices (based on getFillColor method)
        if (c.equals(Color.web("#00FFFF"))) return 1; // Cyan
        if (c.equals(Color.web("#B026FF"))) return 2; // Purple
        if (c.equals(Color.web("#39FF14"))) return 3; // Green
        if (c.equals(Color.web("#FFFF00"))) return 4; // Yellow
        if (c.equals(Color.web("#FF3131"))) return 5; // Red
        if (c.equals(Color.web("#1F51FF"))) return 6; // Blue
        if (c.equals(Color.web("#FF10F0"))) return 7; // Pink
        
        return 0; // Transparent
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

    private void moveDown(MoveEvent event) {
        if (!isPause.get()) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
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
}