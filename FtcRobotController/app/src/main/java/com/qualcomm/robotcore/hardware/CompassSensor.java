package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.HardwareDevice;

public abstract class CompassSensor implements HardwareDevice {
   public abstract boolean calibrationFailed();

   public abstract double getDirection();

   public abstract void setMode(CompassSensor.CompassMode var1);

   public abstract String status();

   public String toString() {
      Object[] var1 = new Object[]{Double.valueOf(this.getDirection())};
      return String.format("Compass: %3.1f", var1);
   }

   public static enum CompassMode {
      CALIBRATION_MODE,
      MEASUREMENT_MODE;

      static {
         CompassSensor.CompassMode[] var0 = new CompassSensor.CompassMode[]{MEASUREMENT_MODE, CALIBRATION_MODE};
      }
   }
}
