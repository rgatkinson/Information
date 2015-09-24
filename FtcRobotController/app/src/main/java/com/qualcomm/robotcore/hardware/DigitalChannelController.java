package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.SerialNumber;

public interface DigitalChannelController extends HardwareDevice
{
    Mode getDigitalChannelMode(int p0);
    
    boolean getDigitalChannelState(int p0);
    
    SerialNumber getSerialNumber();
    
    void setDigitalChannelMode(int p0, Mode p1);
    
    void setDigitalChannelState(int p0, boolean p1);
    
    public enum Mode
    {
        INPUT, 
        OUTPUT;
    }
}
