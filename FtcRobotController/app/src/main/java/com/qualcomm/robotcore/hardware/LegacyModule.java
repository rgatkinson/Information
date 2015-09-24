package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cController;

public interface LegacyModule extends HardwareDevice, I2cController {
   void enable9v(int var1, boolean var2);

   void enableAnalogReadMode(int var1);

   byte[] readAnalog(int var1);

   void setDigitalLine(int var1, int var2, boolean var3);
}
