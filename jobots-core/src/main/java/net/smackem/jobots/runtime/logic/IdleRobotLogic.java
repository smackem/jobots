package net.smackem.jobots.runtime.logic;

import net.smackem.jobots.runtime.RobotLogic;

public enum IdleRobotLogic implements RobotLogic {

    INSTANCE;

    @Override
    public String logicId() {
        return "idle";
    }

    @Override
    public void offerInput(Input input) {
    }

    @Override
    public Output pollOutput() {
        return null;
    }
}
