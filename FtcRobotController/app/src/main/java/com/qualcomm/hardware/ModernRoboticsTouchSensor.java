package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class ModernRoboticsTouchSensor extends TouchSensor
{
    private DeviceInterfaceModule a;
    private int b;
    
    public ModernRoboticsTouchSensor(final DeviceInterfaceModule a, final int b) {
        this.a = null;
        this.b = -1;
        this.a = a;
        this.b = b;
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; digital port " + this.b;
    }
    
    @Override
    public String getDeviceName() {
        return "Modern Robotics Touch Sensor";
    }
    
    @Override
    public double getValue() {
        if (this.isPressed()) {
            return 1.0;
        }
        return 0.0;
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public boolean isPressed() {
        return this.a.getDigitalChannelState(this.b);
    }
}
