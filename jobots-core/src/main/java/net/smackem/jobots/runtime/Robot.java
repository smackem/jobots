package net.smackem.jobots.runtime;

import java.util.Objects;

public class Robot {

    private final RobotLogic logic;
    private final double acceleration;
    private Vector position = Vector.ORIGIN;
    private Vector speed = Vector.ORIGIN;
    private Vector actualSpeed = Vector.ORIGIN;

    public Robot(double acceleration, RobotLogic logic) {
        this.acceleration = acceleration;
        this.logic = Objects.requireNonNull(logic);
    }

    public double acceleration() {
        return this.acceleration;
    }

    public RobotLogic logic() {
        return this.logic;
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
