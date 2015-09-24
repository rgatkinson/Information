package com.qualcomm.robotcore.hardware;

public interface DeviceInterfaceModule extends AnalogInputController, AnalogOutputController, DigitalChannelController, I2cController, PWMOutputController
{
    byte getDigitalIOControlByte();
    
    int getDigitalInputStateByte();
    
    byte getDigitalOutputStateByte();
    
    boolean getLEDState(int p0);
    
    void setDigitalIOControlByte(byte p0);
    
    void setDigitalOutputByte(byte p0);
    
    void setLED(int p0, boolean p1);
}
