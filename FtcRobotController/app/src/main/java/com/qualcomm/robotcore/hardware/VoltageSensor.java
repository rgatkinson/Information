package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.HardwareDevice;

public interface VoltageSensor extends HardwareDevice {
   double getVoltage();
}
