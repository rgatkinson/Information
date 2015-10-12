package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.HardwareDevice;

public abstract class TouchSensorMultiplexer implements HardwareDevice {
   public abstract int getSwitches();

   public abstract boolean isTouchSensorPressed(int var1);
}
