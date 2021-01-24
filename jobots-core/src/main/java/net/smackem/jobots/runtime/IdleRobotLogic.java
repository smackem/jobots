package net.smackem.jobots.runtime;

public enum IdleRobotLogic implements RobotLogic {

    INSTANCE;

    @Override
    public void offerInput(Input input) {
    }

    @Override
    public Output pollOutput() {
        return null;
    }
}
