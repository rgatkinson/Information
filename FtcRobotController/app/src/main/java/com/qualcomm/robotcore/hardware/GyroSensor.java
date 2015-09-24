package com.qualcomm.robotcore.hardware;

public abstract class GyroSensor implements HardwareDevice
{
    public abstract double getRotation();
    
    public abstract String status();
    
    @Override
    public String toString() {
        return String.format("Gyro: %3.1f", this.getRotation());
    }
}
