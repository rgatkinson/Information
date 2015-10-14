package com.qualcomm.robotcore.hardware;

public interface DeviceInterfaceModule extends AnalogInputController, AnalogOutputController, DigitalChannelController, I2cController, PWMOutputController {
    byte getDigitalIOControlByte();

    void setDigitalIOControlByte(byte var1);

    int getDigitalInputStateByte();

    byte getDigitalOutputStateByte();

    boolean getLEDState(int var1);

    void setDigitalOutputByte(byte var1);

    void setLED(int var1, boolean var2);
}
