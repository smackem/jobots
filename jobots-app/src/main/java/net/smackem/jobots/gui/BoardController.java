package net.smackem.jobots.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class BoardController {

    private final Timeline timer;

    public BoardController() {
        this.timer = new Timeline(new KeyFrame(Duration.millis(50), this::tick));
        this.timer.setCycleCount(Animation.INDEFINITE);
    }

    @FXML
    private Canvas canvas;

    @FXML
    private void initialize() {
        render();
        this.timer.play();
    }

    private void render() {
        final GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
    }

    private void tick(ActionEvent ignored) {
        render();
    }
}
