package net.battle.core.handlers.math;

public abstract class MathFunction {
    protected final double a;

    public MathFunction(double a, double b, double m) {
        this.a = a;
        this.b = b;
        this.m = m;
    }

    protected final double b;
    protected final double m;

    public double getA() {
        return this.a;
    }

    public double getB() {
        return this.b;
    }

    public double getM() {
        return this.m;
    }

    public abstract double y(double paramDouble);
}