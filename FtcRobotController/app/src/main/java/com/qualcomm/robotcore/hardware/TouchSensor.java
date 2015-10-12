package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.HardwareDevice;

public abstract class TouchSensor implements HardwareDevice {
   public abstract double getValue();

   public abstract boolean isPressed();

   public String toString() {
      Object[] var1 = new Object[]{Double.valueOf(this.getValue())};
      return String.format("Touch Sensor: %1.2f", var1);
   }
}
