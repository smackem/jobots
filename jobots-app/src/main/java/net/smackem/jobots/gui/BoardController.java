package net.smackem.jobots.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import net.smackem.jobots.runtime.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BoardController {

    private static final int BOARD_WIDTH = 800, BOARD_HEIGHT = 600;
    private final Timeline timer;
    private final Engine engine;

    public BoardController() {
        this.timer = new Timeline(new KeyFrame(Duration.millis(50), this::tick));
        this.timer.setCycleCount(Animation.INDEFINITE);
        final Collection<Robot> robots = createRandomRobots(10);
        final String source = readSourceFromResource("/net/smackem/jobots/runtime/flock.js");
        for (int i = 0; i < 50; i++) {
            robots.add(new Robot(new ThreadedJSRobotLogic(source, "flock-" + i), colorToArgb(Color.RED)));
        }
        positionRobots(robots);
        this.engine = new Engine(new Vector(BOARD_WIDTH, BOARD_HEIGHT), robots);
    }

    @FXML
    private Canvas canvas;

    @FXML
    private void initialize() {
        render();
        this.timer.play();
        Platform.runLater(() -> {
            final Window window = this.canvas.getScene().getWindow();
            window.setOnCloseRequest(this::onWindowClosing);
        });
    }

    private String readSourceFromResource(String resource) {
        final InputStream is = getClass().getResourceAsStream(resource);
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void onWindowClosing(WindowEvent windowEvent) {
        this.engine.close();
    }

    private void render() {
        final GraphicsContext gc = this.canvas.getGraphicsContext2D();
        final Vector dimensions = this.engine.boardDimensions();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, dimensions.x(), dimensions.y());
        for (final Robot robot : this.engine.robots()) {
            final Vector position = robot.getPosition();
            gc.setFill(argbToColor(robot.colorArgb()));
            gc.fillOval(position.x() - 5, position.y() - 5, 10, 10);
        }
    }

    private void tick(ActionEvent ignored) {
        this.engine.tick();
        render();
    }

    private static Collection<Robot> createRandomRobots(int count) {
        final List<Color> robotPaints = List.of(
                Color.AQUAMARINE,
                Color.ORANGE,
                Color.YELLOW,
                Color.YELLOWGREEN,
                Color.BLUE,
                Color.BLUEVIOLET,
                Color.PINK,
                Color.GREEN,
                Color.DARKSEAGREEN,
                Color.CYAN,
                Color.CHOCOLATE);
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        return IntStream.range(0, count)
                .mapToObj(ignored -> {
                    final Color color = robotPaints.get(random.nextInt(0, robotPaints.size()));
                    return new Robot(new RandomRobotLogic(), colorToArgb(color));
                })
                .collect(Collectors.toList());
    }

    private static void positionRobots(Collection<Robot> robots) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        for (final Robot r : robots) {
            r.setPosition(new Vector(random.nextInt(BOARD_WIDTH), random.nextDouble(BOARD_HEIGHT)));
        }
    }

    private static int colorToArgb(Color color) {
        return ((int) (color.getOpacity() * 255) << 24) |
                ((int) (color.getRed() * 255) << 16) |
                ((int) (color.getGreen() * 255) << 8) |
                ((int) (color.getBlue() * 255));
    }

    private static Color argbToColor(int colorArgb) {
        return Color.rgb(
                colorArgb >> 16 & 255,
                colorArgb >> 8 & 255,
                colorArgb & 255,
                (colorArgb >> 24 & 255) / 255.0);
    }
}
