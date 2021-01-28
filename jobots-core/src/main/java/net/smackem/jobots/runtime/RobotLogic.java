package net.smackem.jobots.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface RobotLogic {

    String logicId();
    void offerInput(Input input);
    Output pollOutput();

    class DetectedRobot {
        private final String type;
        private final Vector position;

        public DetectedRobot(String type, Vector position) {
            this.type = type;
            this.position = position;
        }

        public String type() {
            return this.type;
        }

        public Vector position() {
            return this.position;
        }
    }

    class Input {
        private final Vector position;
        private final Vector boardDimensions;
        private final List<DetectedRobot> neighbours;

        public Input(Vector position, Vector boardDimensions, Collection<DetectedRobot> neighbours) {
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

        public List<DetectedRobot> neighbours() {
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
