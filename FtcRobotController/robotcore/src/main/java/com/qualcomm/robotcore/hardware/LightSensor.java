package com.qualcomm.robotcore.hardware;

public abstract class LightSensor implements HardwareDevice {
    public abstract void enableLed(boolean var1);

    public abstract double getLightDetected();

    public abstract int getLightDetectedRaw();

    public abstract String status();

    public String toString() {
        Object[] var1 = new Object[]{Double.valueOf(this.getLightDetected())};
        return String.format("Light Level: %1.2f", var1);
    }
}
