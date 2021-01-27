package net.smackem.jobots.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class Engine implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(Engine.class);
    private static final double MAX_ROBOT_SPEED = 300;
    private final Vector boardDimensions;
    private final Collection<Robot> robots;

    public Engine(Vector boardDimensions, Collection<Robot> robots) {
        this.boardDimensions = Objects.requireNonNull(boardDimensions);
        this.robots = List.copyOf(Objects.requireNonNull(robots));
    }

    public Vector boardDimensions() {
        return this.boardDimensions;
    }

    public Collection<Robot> robots() {
        return this.robots;
    }

    public void tick() {
        for (final Robot robot : this.robots) {
            final RobotLogic.Output output = robot.logic().pollOutput();
            if (output != null) {
                robot.setSpeed(coerceSpeed(output.speed()));
            }
        }

        for (final Robot robot : this.robots) {
            final Vector actualSpeed = robot.getActualSpeed();
            robot.setPosition(robot.getPosition().add(actualSpeed));
            final Vector speed = robot.getSpeed();
            if (speed.equals(actualSpeed)) {
                continue;
            }
            double newSpeedX = speed.x(), newSpeedY = speed.y();
            if (speed.x() > actualSpeed.x()) {
                newSpeedX = Math.min(actualSpeed.x() + robot.acceleration(), speed.x());
            } else if (speed.x() < actualSpeed.x()) {
                newSpeedX = Math.max(actualSpeed.x() - robot.acceleration(), speed.x());
            }
            if (speed.y() > actualSpeed.y()) {
                newSpeedY = Math.min(actualSpeed.y() + robot.acceleration(), speed.y());
            } else if (speed.y() < actualSpeed.y()) {
                newSpeedY = Math.max(actualSpeed.y() - robot.acceleration(), speed.y());
            }
            robot.setActualSpeed(new Vector(newSpeedX, newSpeedY));
        }

        for (final Robot robot : this.robots) {
            robot.logic().offerInput(new RobotLogic.Input(
                    robot.getPosition(),
                    this.boardDimensions,
                    this.robots.stream()
                            .filter(r -> r != robot)
                            .map(Robot::getPosition)
                            .collect(Collectors.toList())));
        }
    }

    private static Vector coerceSpeed(Vector speed) {
        double x = speed.x();
        double y = speed.y();
        if (x < -MAX_ROBOT_SPEED) {
            x = -MAX_ROBOT_SPEED;
        } else if (x > MAX_ROBOT_SPEED) {
            x = MAX_ROBOT_SPEED;
        }
        if (y < -MAX_ROBOT_SPEED) {
            y = -MAX_ROBOT_SPEED;
        } else if (y > MAX_ROBOT_SPEED) {
            y = MAX_ROBOT_SPEED;
        }
        return new Vector(x / 30.0, y / 30.0);
    }

    @Override
    public void close() {
        for (final Robot robot : this.robots()) {
            try {
                robot.close();
            } catch (Exception e) {
                log.warn("error closing " + robot, e);
            }
        }
    }
}
