package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.util.SerialNumber;

public interface DigitalChannelController extends HardwareDevice {
   DigitalChannelController.Mode getDigitalChannelMode(int var1);

   boolean getDigitalChannelState(int var1);

   SerialNumber getSerialNumber();

   void setDigitalChannelMode(int var1, DigitalChannelController.Mode var2);

   void setDigitalChannelState(int var1, boolean var2);

   public static enum Mode {
      INPUT,
      OUTPUT;

      static {
         DigitalChannelController.Mode[] var0 = new DigitalChannelController.Mode[]{INPUT, OUTPUT};
      }
   }
}
