package com.qualcomm.robotcore.hardware;

public static class IrSeekerIndividualSensor
{
    private double a;
    private double b;
    
    public IrSeekerIndividualSensor() {
        this(0.0, 0.0);
    }
    
    public IrSeekerIndividualSensor(final double a, final double b) {
        this.a = 0.0;
        this.b = 0.0;
        this.a = a;
        this.b = b;
    }
    
    public double getSensorAngle() {
        return this.a;
    }
    
    public double getSensorStrength() {
        return this.b;
    }
    
    @Override
    public String toString() {
        return String.format("IR Sensor: %3.1f degrees at %3.1f%% power", this.a, 100.0 * this.b);
    }
}
