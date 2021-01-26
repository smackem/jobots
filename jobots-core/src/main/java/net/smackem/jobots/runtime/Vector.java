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

    public Vector add(Vector right) {
        return new Vector(this.x + right.x, this.y + right.y);
    }

    public Vector subtract(Vector right) {
        return new Vector(this.x - right.x, this.y - right.y);
    }

    public Vector multiplyWith(double scalar) {
        return new Vector(this.x * scalar, this.y * scalar);
    }

    public Vector divideBy(double scalar) {
        return new Vector(this.x / scalar, this.y / scalar);
    }

    public static double distance(Vector a, Vector b) {
        return Math.hypot(a.x() - b.x(), a.y() - b.y());
    }

    public static double angleBetween(Vector a, Vector b) {
        // via "perpendicular dot product"
        return Math.atan2(a.x * b.y - a.y * b.x, a.x * b.x + a.y * b.y);
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
