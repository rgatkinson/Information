package com.qualcomm.robotcore.hardware;

public abstract class TouchSensor implements HardwareDevice
{
    public abstract double getValue();
    
    public abstract boolean isPressed();
    
    @Override
    public String toString() {
        return String.format("Touch Sensor: %1.2f", this.getValue());
    }
}
