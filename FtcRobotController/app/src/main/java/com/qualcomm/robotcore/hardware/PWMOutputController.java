package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.util.SerialNumber;

public interface PWMOutputController extends HardwareDevice {
   int getPulseWidthOutputTime(int var1);

   int getPulseWidthPeriod(int var1);

   SerialNumber getSerialNumber();

   void setPulseWidthOutputTime(int var1, int var2);

   void setPulseWidthPeriod(int var1, int var2);
}
