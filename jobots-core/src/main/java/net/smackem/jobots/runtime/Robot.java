package net.smackem.jobots.runtime;

import java.util.Objects;

public class Robot {

    private final RobotController controller;
    private final double acceleration;
    private Vector position = Vector.ORIGIN;
    private Vector speed = Vector.ORIGIN;
    private Vector actualSpeed = Vector.ORIGIN;

    public Robot(double acceleration, RobotController controller) {
        this.acceleration = acceleration;
        this.controller = Objects.requireNonNull(controller);
    }

    public double acceleration() {
        return this.acceleration;
    }

    public RobotController controller() {
        return this.controller;
    }

    public Vector getPosition() {
        return this.position;
    }

    public void setPosition(Vector value) {
        this.position = Objects.requireNonNull(value);
    }

    public Vector getSpeed() {
        return this.speed;
    }

    public void setSpeed(Vector value) {
        this.speed = Objects.requireNonNull(value);
    }

    public Vector getActualSpeed() {
        return this.actualSpeed;
    }

    public void setActualSpeed(Vector value) {
        this.actualSpeed = Objects.requireNonNull(value);
    }
}
