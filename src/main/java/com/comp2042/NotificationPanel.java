package com.comp2042;

import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class NotificationPanel extends BorderPane {

    public NotificationPanel(String text, boolean isCombo) {
        setMinHeight(200);
        setMinWidth(220);
        final Label score = new Label(text);
        
        // Smaller, clearer font
        score.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-font-family: 'Arial Black', 'Impact', sans-serif;");
        
        // Choose color based on whether it's a combo
        Color textColor = isCombo ? Color.web("#FF10F0") : Color.web("#00FFFF"); // Pink for combo, cyan for normal
        score.setTextFill(textColor);
        
        // Clearer, less intense glow effect
        DropShadow glow = new DropShadow();
        glow.setColor(textColor);
        glow.setRadius(12);
        glow.setSpread(0.4);
        score.setEffect(glow);
        
        // Better text rendering
        score.setCache(true);
        
        setCenter(score);
    }

    public void showScore(ObservableList<Node> list) {
        // Scale animation - start big and shrink slightly
        ScaleTransition st = new ScaleTransition(Duration.millis(300), this);
        st.setFromX(1.5);
        st.setFromY(1.5);
        st.setToX(1.0);
        st.setToY(1.0);
        
        // Fade out
        FadeTransition ft = new FadeTransition(Duration.millis(1500), this);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setDelay(Duration.millis(500));
        
        // Float up
        TranslateTransition tt = new TranslateTransition(Duration.millis(2000), this);
        tt.setToY(this.getLayoutY() - 60);
        
        ParallelTransition transition = new ParallelTransition(st, tt, ft);
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.remove(NotificationPanel.this);
            }
        });
        transition.play();
    }
}
