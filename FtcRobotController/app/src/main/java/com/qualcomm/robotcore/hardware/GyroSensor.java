package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.HardwareDevice;

public abstract class GyroSensor implements HardwareDevice {
   public abstract void calibrate();

   public abstract int getHeading();

   public abstract double getRotation();

   public abstract boolean isCalibrating();

   public void notSupported() {
      throw new UnsupportedOperationException("This method is not supported for " + this.getDeviceName());
   }

   public abstract int rawX();

   public abstract int rawY();

   public abstract int rawZ();

   public abstract void resetZAxisIntegrator();

   public abstract String status();

   public String toString() {
      Object[] var1 = new Object[]{Double.valueOf(this.getRotation())};
      return String.format("Gyro: %3.1f", var1);
   }
}
