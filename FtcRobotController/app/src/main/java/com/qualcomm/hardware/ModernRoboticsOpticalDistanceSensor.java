package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

public class ModernRoboticsOpticalDistanceSensor extends OpticalDistanceSensor
{
    private final ModernRoboticsUsbDeviceInterfaceModule a;
    private final int b;
    
    public ModernRoboticsOpticalDistanceSensor(final ModernRoboticsUsbDeviceInterfaceModule a, final int b) {
        this.a = a;
        this.b = b;
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public void enableLed(final boolean b) {
    }
    
    @Override
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; analog port " + this.b;
    }
    
    @Override
    public String getDeviceName() {
        return "Modern Robotics Optical Distance Sensor";
    }
    
    @Override
    public double getLightDetected() {
        return this.a.getAnalogInputValue(this.b) / 1023.0;
    }
    
    @Override
    public int getLightDetectedRaw() {
        return this.a.getAnalogInputValue(this.b);
    }
    
    @Override
    public int getVersion() {
        return 0;
    }
    
    @Override
    public String status() {
        return String.format("Optical Distance Sensor, connected via device %s, port %d", this.a.getSerialNumber().toString(), this.b);
    }
}
