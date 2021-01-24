package net.smackem.jobots.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class Engine {
    private static final Logger log = LoggerFactory.getLogger(Engine.class);
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
                Vector speed = output.speed();
                speed = speed.length() > 10
                        ? speed.normalize().multiplyWith(10)
                        : speed;
                log.info("new speed: {}", speed);
                robot.setSpeed(speed);
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
}
