package com.qualcomm.robotcore.hardware;

public abstract class TouchSensorMultiplexer implements HardwareDevice
{
    public abstract int getSwitches();
    
    public abstract boolean isTouchSensorPressed(final int p0);
}
