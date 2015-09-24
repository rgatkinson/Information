package com.qualcomm.robotcore.hardware;

public class DigitalChannel implements HardwareDevice
{
    private DigitalChannelController a;
    private int b;
    
    public DigitalChannel(final DigitalChannelController a, final int b) {
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
        return "Digital Channel";
    }
    
    public DigitalChannelController.Mode getMode() {
        return this.a.getDigitalChannelMode(this.b);
    }
    
    public boolean getState() {
        return this.a.getDigitalChannelState(this.b);
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    public void setMode(final DigitalChannelController.Mode mode) {
        this.a.setDigitalChannelMode(this.b, mode);
    }
    
    public void setState(final boolean b) {
        this.a.setDigitalChannelState(this.b, b);
    }
}
