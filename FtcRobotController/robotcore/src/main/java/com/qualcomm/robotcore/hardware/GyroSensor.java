package com.qualcomm.robotcore.hardware;

public abstract class GyroSensor implements HardwareDevice {
    public abstract double getRotation();

    public abstract String status();

    public String toString() {
        Object[] var1 = new Object[]{Double.valueOf(this.getRotation())};
        return String.format("Gyro: %3.1f", var1);
    }
}
