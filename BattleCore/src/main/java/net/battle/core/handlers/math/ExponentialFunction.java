package net.battle.core.handlers.math;

public class ExponentialFunction extends MathFunction {
    public ExponentialFunction(double a, double b) {
        super(a, b, -1.0D);
    }

    public double y(double x) {
        return this.a * Math.pow(this.b, x);
    }
}