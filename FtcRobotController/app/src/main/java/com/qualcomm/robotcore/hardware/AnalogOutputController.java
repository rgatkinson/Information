package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.util.SerialNumber;

public interface AnalogOutputController extends HardwareDevice {
   SerialNumber getSerialNumber();

   void setAnalogOutputFrequency(int var1, int var2);

   void setAnalogOutputMode(int var1, byte var2);

   void setAnalogOutputVoltage(int var1, int var2);
}
