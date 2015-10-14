package com.qualcomm.robotcore.hardware;

public abstract class CompassSensor implements HardwareDevice {
    public abstract boolean calibrationFailed();

    public abstract double getDirection();

    public abstract void setMode(CompassMode var1);

    public abstract String status();

    public String toString() {
        Object[] var1 = new Object[]{Double.valueOf(this.getDirection())};
        return String.format("Compass: %3.1f", var1);
    }

    public enum CompassMode {
        CALIBRATION_MODE,
        MEASUREMENT_MODE;

        static {
            CompassMode[] var0 = new CompassMode[]{MEASUREMENT_MODE, CALIBRATION_MODE};
        }
    }
}
