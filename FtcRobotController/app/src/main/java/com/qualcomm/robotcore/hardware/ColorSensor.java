package com.qualcomm.robotcore.hardware;

public abstract class ColorSensor implements HardwareDevice
{
    public abstract int alpha();
    
    public abstract int argb();
    
    public abstract int blue();
    
    public abstract void enableLed(final boolean p0);
    
    public abstract int green();
    
    public abstract int red();
    
    @Override
    public String toString() {
        return String.format("argb: %d", this.argb());
    }
}
