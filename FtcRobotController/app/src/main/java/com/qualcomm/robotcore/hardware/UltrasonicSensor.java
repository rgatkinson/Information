package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.HardwareDevice;

public abstract class UltrasonicSensor implements HardwareDevice {
   public abstract double getUltrasonicLevel();

   public abstract String status();

   public String toString() {
      Object[] var1 = new Object[]{Double.valueOf(this.getUltrasonicLevel())};
      return String.format("Ultrasonic: %6.1f", var1);
   }
}
