package com.qualcomm.robotcore.util;

public class DifferentialControlLoopCoefficients {
    public double d = 0.0D;
    public double i = 0.0D;
    public double p = 0.0D;

    public DifferentialControlLoopCoefficients() {
    }

    public DifferentialControlLoopCoefficients(double var1, double var3, double var5) {
        this.p = var1;
        this.i = var3;
        this.d = var5;
    }
}
