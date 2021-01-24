package net.smackem.jobots.runtime;

import java.util.Objects;

public class Vector {
    private final double x;
    private final double y;

    public static final Vector ORIGIN = new Vector(0, 0);

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }

    public double length() {
        return Math.hypot(this.x, this.y);
    }

    public Vector negate() {
        return new Vector(-this.x, -this.y);
    }

    public static Vector add(Vector a, Vector b) {
        return new Vector(a.x + b.x, a.y + b.y);
    }

    public static Vector subtract(Vector a, Vector b) {
        return new Vector(a.x - b.x, a.y - b.y);
    }

    public static Vector multiply(Vector v, double scalar) {
        return new Vector(v.x * scalar, v.y * scalar);
    }

    public static Vector divide(Vector v, double scalar) {
        return new Vector(v.x / scalar, v.y / scalar);
    }

    public Vector normalize() {
        final double length = length();
        return length == 0
                ? ORIGIN
                : new Vector(this.x / length, this.y / length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Vector vector = (Vector) o;
        return Double.compare(vector.x, x) == 0 && Double.compare(vector.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Vector{" +
               "x=" + x +
               ", y=" + y +
               '}';
    }
}
