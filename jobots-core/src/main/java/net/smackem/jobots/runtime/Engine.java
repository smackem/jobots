package net.smackem.jobots.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Engine {
    private final Collection<Robot> robots;

    public Engine(Collection<Robot> robots) {
        this.robots = new ArrayList<>(Objects.requireNonNull(robots));
    }


}
