package com.qualcomm.robotcore.hardware;

public abstract class CompassSensor implements HardwareDevice
{
    public abstract boolean calibrationFailed();
    
    public abstract double getDirection();
    
    public abstract void setMode(final CompassMode p0);
    
    public abstract String status();
    
    @Override
    public String toString() {
        return String.format("Compass: %3.1f", this.getDirection());
    }
    
    public enum CompassMode
    {
        CALIBRATION_MODE, 
        MEASUREMENT_MODE;
    }
}
