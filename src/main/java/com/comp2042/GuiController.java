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
import javafx.scene.effect.Glow;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GridPane nextBrickPanel;

    private Rectangle[][] nextBrickDisplay;

    @FXML
    private GameOverPanel gameOverPanel;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty(false);

    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    private Label pauseLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
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

                        // Display the score notification for line clears
                        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                            NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                            groupNotification.getChildren().add(notificationPanel);
                            notificationPanel.showScore(groupNotification.getChildren());
                        }

                        refreshBrick(downData.getViewData());
                        refreshNextBrick(downData.getViewData().getNextBrickData());
                        keyEvent.consume();
                    }

                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                }

                // Start a new game
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }

                // Toggle pause / resume
                if (keyEvent.getCode() == KeyCode.P) {
                    togglePause();
                    keyEvent.consume();
                }
            }
        });

        gameOverPanel.setVisible(false);

        pauseLabel = new Label("PAUSED");
        pauseLabel.setVisible(false);
        pauseLabel.setStyle(
                "-fx-font-size: 32px;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-effect: dropshadow(gaussian, black, 10, 0.5, 0, 0);"
        );
        groupNotification.getChildren().add(pauseLabel);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
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
        brickPanel.setLayoutX(
                gamePanel.getLayoutX()
                        + brick.getxPosition() * brickPanel.getVgap()
                        + brick.getxPosition() * BRICK_SIZE
        );
        brickPanel.setLayoutY(
                -42 + gamePanel.getLayoutY()
                        + brick.getyPosition() * brickPanel.getHgap()
                        + brick.getyPosition() * BRICK_SIZE
        );

        refreshNextBrick(brick.getNextBrickData());

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.AQUA;
                break;
            case 2:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 3:
                returnPaint = Color.DARKGREEN;
                break;
            case 4:
                returnPaint = Color.YELLOW;
                break;
            case 5:
                returnPaint = Color.RED;
                break;
            case 6:
                returnPaint = Color.BEIGE;
                break;
            case 7:
                returnPaint = Color.BURLYWOOD;
                break;
            case 8:
                returnPaint = Color.GRAY.deriveColor(1, 1, 1, 0.4);
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }

    private void refreshBrick(ViewData brick) {
        // FIXED: Corrected boolean logic. Now updates when NOT paused.
        if (!isPause.get()) {
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);

            // FIXED: Now getting the current BRICK data, not the NEXT brick data.
            int[][] currentBrickData = brick.getBrickData();

            for (int i = 0; i < rectangles.length; i++) {
                for (int j = 0; j < rectangles[i].length; j++) {
                    setRectangleData(0, rectangles[i][j]); // Set to TRANSPARENT (color 0)
                }
            }

            for (int i = 0; i < currentBrickData.length; i++) {
                for (int j = 0; j < currentBrickData[i].length; j++) {
                    setRectangleData(currentBrickData[i][j], rectangles[i][j]);
                }
            }
        }
    }

    private void refreshNextBrick(int[][] nextBrickData) {
        if (nextBrickDisplay == null) {
            nextBrickDisplay = new Rectangle[nextBrickData.length][nextBrickData[0].length];
            for (int i = 0; i < nextBrickData.length; i++) {
                for (int j = 0; j < nextBrickData[i].length; j++) {
                    Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    rectangle.setFill(Color.TRANSPARENT); // Ensure initial transparency
                    nextBrickDisplay[i][j] = rectangle;
                    nextBrickPanel.add(rectangle, j, i);
                }
            }
        }

        for (int i = 0; i < nextBrickData.length; i++) {
            for (int j = 0; j < nextBrickData[i].length; j++) {
                setRectangleData(nextBrickData[i][j], nextBrickDisplay[i][j]);
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

    private void setRectangleData(int color, Rectangle rectangle) {
        Paint fill = getFillColor(color);
        rectangle.setFill(fill);
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);

        if (color == 0) {
            rectangle.setEffect(null);
        } else {
            DropShadow shadow = new DropShadow();
            shadow.setRadius(8);
            shadow.setSpread(0.3);
            shadow.setColor(Color.color(0, 0, 0, 0.5));

            Glow glow = new Glow(0.3);
            glow.setInput(shadow);

            rectangle.setEffect(glow);
        }
    }


    private void moveDown(MoveEvent event) {
        // FIXED: Corrected boolean logic.
        if (!isPause.get()) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel =
                        new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
            refreshNextBrick(downData.getViewData().getNextBrickData());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        // Intentionally left empty – binding handled elsewhere if needed
    }

    public void gameOver() {
        if (timeLine != null) {
            timeLine.stop();
        }
        gameOverPanel.setVisible(true);
        isGameOver.set(true);
        hidePauseOverlay();
    }

    public void newGame(ActionEvent actionEvent) {
        if (timeLine != null) {
            timeLine.stop();
        }
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        if (timeLine != null) {
            timeLine.play();
        }
        isPause.set(false);
        isGameOver.set(false);
        hidePauseOverlay();
    }

    public void pauseGame(ActionEvent actionEvent) {
        togglePause();
    }


    private void togglePause() {
        if (isGameOver.get()) {
            return;
        }

        if (isPause.get()) {
            if (timeLine != null) {
                timeLine.play();
            }
            isPause.set(false);
            hidePauseOverlay();
        } else {
            if (timeLine != null) {
                timeLine.pause();
            }
            isPause.set(true);
            showPauseOverlay();
        }

        gamePanel.requestFocus();
    }

    private void showPauseOverlay() {
        if (pauseLabel != null) {
            pauseLabel.setLayoutX(gamePanel.getLayoutX() + 40);
            pauseLabel.setLayoutY(gamePanel.getLayoutY() + 80);
            pauseLabel.setVisible(true);
        }
    }

    private void hidePauseOverlay() {
        if (pauseLabel != null) {
            pauseLabel.setVisible(false);
        }
    }
}