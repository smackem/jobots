package net.smackem.jobots.runtime.logic;

import net.smackem.jobots.runtime.RobotLogic;
import net.smackem.jobots.runtime.Vector;

import java.util.Comparator;
import java.util.Objects;

public class FlockingRobotLogic implements RobotLogic {

    private Vector position;
    private Vector destination;

    @Override
    public String logicId() {
        return "flock";
    }

    @Override
    public void offerInput(Input input) {
        this.position = input.position();
        final DetectedRobot nearestNonFlocking = input.neighbours().stream()
                .filter(r -> Objects.equals(r.type(), logicId()) == false)
                .min(Comparator.comparing(r -> Vector.distance(r.position(), this.position)))
                .orElse(null);
        if (nearestNonFlocking == null) {
            return;
        }
        final DetectedRobot nearestFlocking = input.neighbours().stream()
                .filter(r -> Objects.equals(r.type(), logicId()))
                .min(Comparator.comparing(r -> Vector.distance(r.position(), this.position)))
                .orElse(null);
        Vector direction = nearestNonFlocking.position().subtract(this.position);
        if (nearestFlocking != null) {
            final double distance = Vector.distance(nearestFlocking.position(), this.position);
            if (distance < 100) {
                final Vector offset = nearestFlocking.position()
                        .subtract(this.position)
                        .negate()
                        .normalize()
                        .multiplyWith(100 - distance);
                direction = direction.add(offset);
            }
        }
        this.destination = this.position.add(direction);
    }

    @Override
    public Output pollOutput() {
        if (this.position == null || this.destination == null) {
            return null;
        }
        return new Output(this.destination.subtract(this.position));
    }
}
