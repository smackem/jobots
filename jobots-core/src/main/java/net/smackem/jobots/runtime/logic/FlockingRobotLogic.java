package net.smackem.jobots.runtime.logic;

import net.smackem.jobots.runtime.RobotLogic;
import net.smackem.jobots.runtime.Vector;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FlockingRobotLogic implements RobotLogic {

    private Vector position;
    private Vector destination;

    @Override
    public String logicId() {
        return "flock";
    }

    @Override
    public void offerInput(Input input) {
        final double prefDistance = 100;
        this.position = input.position();
        final DetectedRobot nearestNonFlocking = input.neighbours().stream()
                .filter(r -> Objects.equals(r.type(), logicId()) == false)
                .min(Comparator.comparing(r -> r.position().distanceTo(this.position)))
                .orElse(null);
        if (nearestNonFlocking == null) {
            return;
        }
        Vector direction = nearestNonFlocking.position().minus(this.position);
        final List<DetectedRobot> nearestFlocking = input.neighbours().stream()
                .filter(r -> Objects.equals(r.type(), logicId()))
                .sorted(Comparator.comparing(r -> r.position().distanceTo(this.position)))
                .takeWhile(r -> r.position().distanceTo(this.position) < prefDistance)
                .limit(2)
                .collect(Collectors.toList());
        if (nearestFlocking.size() == 1) {
            final Vector nearestPosition = nearestFlocking.get(0).position();
            final double distance = nearestPosition.distanceTo(this.position);
            if (distance < prefDistance) {
                final Vector offset = nearestPosition
                        .minus(this.position)
                        .negate()
                        .normalize()
                        .scale(prefDistance - distance);
                direction = direction.plus(offset);
            }
        } else if (nearestFlocking.size() == 2) {
            final Vector nearestTwo = nearestFlocking.get(0).position().minus(nearestFlocking.get(1).position());
            final Vector perp1 = new Vector(-nearestTwo.y(), nearestTwo.x());
            final Vector perp2 = perp1.negate();
            final Vector perp = perp1.distanceTo(this.position) < perp2.distanceTo(this.position) ? perp1 : perp2;
            final Vector offset = perp
                    .normalize()
                    .scale(prefDistance - nearestFlocking.get(0).position().distanceTo(this.position));
            direction = direction.plus(offset);
        }
        this.destination = this.position.plus(direction);
    }

    @Override
    public Output pollOutput() {
        if (this.position == null || this.destination == null) {
            return null;
        }
        return new Output(this.destination.minus(this.position));
    }
}
