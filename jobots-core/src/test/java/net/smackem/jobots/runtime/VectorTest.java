package net.smackem.jobots.runtime;

import org.assertj.core.data.Percentage;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

public class VectorTest {

    @Test
    public void distance() {
        assertThat(Vector.distance(Vector.ORIGIN, Vector.ORIGIN)).isEqualTo(0.0);
        assertThat(Vector.distance(Vector.ORIGIN, new Vector(1, 0))).isEqualTo(1.0);
        assertThat(Vector.distance(Vector.ORIGIN, new Vector(0, -1))).isEqualTo(1.0);
        assertThat(Vector.distance(Vector.ORIGIN, new Vector(1, 1))).isEqualTo(Math.sqrt(2));
    }

    @Test
    public void angleBetween() {
        // 90 deg
        assertThat(Vector.angleBetween(new Vector(1, 0), new Vector(0, 1))).isEqualTo(Math.PI/2);
        // -90 deg
        assertThat(Vector.angleBetween(new Vector(-1, 0), new Vector(0, 1))).isEqualTo(-Math.PI/2);
        // -180 deg
        assertThat(Vector.angleBetween(new Vector(-1, 0), new Vector(1, 0))).isEqualTo(-Math.PI);
        // 90 deg
        assertThat(Vector.angleBetween(new Vector(-1, 0), new Vector(0, -1))).isEqualTo(Math.PI/2);
    }

    @Test
    public void normalize() {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < 20; i++) {
            final Vector v = new Vector(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000));
            assertThat(v.normalize().length()).isCloseTo(1, Percentage.withPercentage(0.1));
        }
    }
}