package net.smackem.jobots.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class Engine {
    private final Vector boardDimensions;
    private final Collection<Robot> robots;

    public Engine(Vector boardDimensions, Collection<Robot> robots) {
        this.boardDimensions = Objects.requireNonNull(boardDimensions);
        this.robots = new ArrayList<>(Objects.requireNonNull(robots));
    }

    public void tick() {
        for (final Robot robot : this.robots) {
            final RobotController.Output output = robot.controller().pollOutput();
            if (output != null) {
                robot.setSpeed(output.speed());
            }
        }

        for (final Robot robot : this.robots) {
            final Vector actualSpeed = robot.getActualSpeed();
            robot.setPosition(Vector.add(robot.getPosition(), robot.getActualSpeed()));
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
            robot.controller().offerInput(new RobotController.Input(
                    robot.getPosition(),
                    this.boardDimensions,
                    this.robots.stream()
                            .filter(r -> r != robot)
                            .map(Robot::getPosition)
                            .collect(Collectors.toList())));
        }
    }
}
