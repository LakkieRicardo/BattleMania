package net.battle.core.handlers.math;

public class LinearFunction extends MathFunction {
    public LinearFunction(double b, double m) {
        super(-1.0D, b, m);
    }

    public double y(double x) {
        return this.m * x + this.b;
    }
}