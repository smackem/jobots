package net.smackem.jobots.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface RobotLogic {

    void offerInput(Input input);
    Output pollOutput();

    class Input {
        private final Vector position;
        private final Vector boardDimensions;
        private final List<Vector> neighbours;

        public Input(Vector position, Vector boardDimensions, Collection<Vector> neighbours) {
            this.position = position;
            this.boardDimensions = boardDimensions;
            this.neighbours = new ArrayList<>(neighbours);
        }

        public Vector position() {
            return this.position;
        }

        public Vector boardDimensions() {
            return this.boardDimensions;
        }

        public List<Vector> neighbours() {
            return this.neighbours;
        }
    }

    class Output {
        private final Vector speed;

        public Output(Vector speed) {
            this.speed = speed;
        }

        public Vector speed() {
            return this.speed;
        }
    }
}
