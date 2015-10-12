package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.LightSensor;

public abstract class OpticalDistanceSensor extends LightSensor {
   public String toString() {
      Object[] var1 = new Object[]{Double.valueOf(this.getLightDetected())};
      return String.format("OpticalDistanceSensor: %d", var1);
   }
}
