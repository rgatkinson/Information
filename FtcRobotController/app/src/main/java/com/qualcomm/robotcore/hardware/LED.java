package com.qualcomm.robotcore.hardware;

public class LED implements HardwareDevice
{
    private DigitalChannelController a;
    private int b;
    
    public LED(final DigitalChannelController a, final int b) {
        this.a = null;
        this.b = -1;
        (this.a = a).setDigitalChannelMode(this.b = b, DigitalChannelController.Mode.OUTPUT);
    }
    
    @Override
    public void close() {
    }
    
    public void enable(final boolean b) {
        this.a.setDigitalChannelState(this.b, b);
    }
    
    @Override
    public String getConnectionInfo() {
        return null;
    }
    
    @Override
    public String getDeviceName() {
        return null;
    }
    
    @Override
    public int getVersion() {
        return 0;
    }
}
