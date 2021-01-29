package net.smackem.jobots.runtime.logic;

import net.smackem.jobots.runtime.RobotLogic;
import net.smackem.jobots.runtime.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

public class RandomRobotLogic implements RobotLogic {
    private static final Logger log = LoggerFactory.getLogger(RandomRobotLogic.class);
    private Vector destination;
    private Vector position;

    @Override
    public String logicId() {
        return "random";
    }

    @Override
    public void offerInput(Input input) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        this.position = input.position();
        if (this.destination == null || Vector.distance(this.destination, this.position) < 10) {
            this.destination = new Vector(
                    random.nextDouble(input.boardDimensions().x()),
                    random.nextDouble(input.boardDimensions().y()));
            log.info("new destination: {}", this.destination);
        }
    }

    @Override
    public Output pollOutput() {
        if (this.position == null) {
            return new Output(Vector.ORIGIN);
        }
        final Vector speed = this.destination.minus(this.position);
        return new Output(speed);
    }
}
