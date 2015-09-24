package com.qualcomm.robotcore.hardware;

public class AnalogOutput implements HardwareDevice
{
    private AnalogOutputController a;
    private int b;
    
    public AnalogOutput(final AnalogOutputController a, final int b) {
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
        return "Analog Output";
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    public void setAnalogOutputFrequency(final int n) {
        this.a.setAnalogOutputFrequency(this.b, n);
    }
    
    public void setAnalogOutputMode(final byte b) {
        this.a.setAnalogOutputMode(this.b, b);
    }
    
    public void setAnalogOutputVoltage(final int n) {
        this.a.setAnalogOutputVoltage(this.b, n);
    }
}
