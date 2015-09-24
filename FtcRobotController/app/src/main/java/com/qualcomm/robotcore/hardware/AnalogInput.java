package com.qualcomm.robotcore.hardware;

public class AnalogInput implements HardwareDevice
{
    private AnalogInputController a;
    private int b;
    
    public AnalogInput(final AnalogInputController a, final int b) {
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
        return this.a.getConnectionInfo() + "; analog port " + this.b;
    }
    
    @Override
    public String getDeviceName() {
        return "Analog Input";
    }
    
    public int getValue() {
        return this.a.getAnalogInputValue(this.b);
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
}
