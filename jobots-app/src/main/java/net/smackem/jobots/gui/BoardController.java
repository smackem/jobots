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
import net.smackem.jobots.runtime.*;

import java.util.List;

public class BoardController {

    private final Timeline timer;
    private final Engine engine;

    public BoardController() {
        this.timer = new Timeline(new KeyFrame(Duration.millis(50), this::tick));
        this.timer.setCycleCount(Animation.INDEFINITE);
        this.engine = new Engine(new Vector(800, 600), List.of(
                new Robot(1.0, new RandomRobotLogic())
        ));
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
        final Vector dimensions = this.engine.boardDimensions();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, dimensions.x(), dimensions.y());
        for (final Robot robot : this.engine.robots()) {
            final Vector position = robot.getPosition();
            gc.setFill(Color.PINK);
            gc.fillOval(position.x() - 5, position.y() - 5, 10, 10);
        }
    }

    private void tick(ActionEvent ignored) {
        this.engine.tick();
        render();
    }
}
