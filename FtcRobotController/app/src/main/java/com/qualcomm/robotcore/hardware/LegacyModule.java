package com.qualcomm.robotcore.hardware;

public interface LegacyModule extends HardwareDevice, I2cController
{
    void enable9v(int p0, boolean p1);
    
    void enableAnalogReadMode(int p0);
    
    byte[] readAnalog(int p0);
    
    void setDigitalLine(int p0, int p1, boolean p2);
}
