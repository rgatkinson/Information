package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.AnalogOutputController;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.PWMOutputController;

public interface DeviceInterfaceModule extends AnalogInputController, AnalogOutputController, DigitalChannelController, I2cController, PWMOutputController {
   byte getDigitalIOControlByte();

   int getDigitalInputStateByte();

   byte getDigitalOutputStateByte();

   boolean getLEDState(int var1);

   void setDigitalIOControlByte(byte var1);

   void setDigitalOutputByte(byte var1);

   void setLED(int var1, boolean var2);
}
