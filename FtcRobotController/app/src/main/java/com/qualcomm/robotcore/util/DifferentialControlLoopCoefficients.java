package com.qualcomm.robotcore.util;

public class DifferentialControlLoopCoefficients
{
    public double d;
    public double i;
    public double p;
    
    public DifferentialControlLoopCoefficients() {
        this.p = 0.0;
        this.i = 0.0;
        this.d = 0.0;
    }
    
    public DifferentialControlLoopCoefficients(final double p3, final double i, final double d) {
        this.p = 0.0;
        this.i = 0.0;
        this.d = 0.0;
        this.p = p3;
        this.i = i;
        this.d = d;
    }
}
