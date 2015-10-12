package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.HardwareDevice;

public abstract class AccelerationSensor implements HardwareDevice {
   public abstract AccelerationSensor.Acceleration getAcceleration();

   public abstract String status();

   public String toString() {
      return this.getAcceleration().toString();
   }

   public static class Acceleration {
      public double x;
      public double y;
      public double z;

      public Acceleration() {
         this(0.0D, 0.0D, 0.0D);
      }

      public Acceleration(double var1, double var3, double var5) {
         this.x = var1;
         this.y = var3;
         this.z = var5;
      }

      public String toString() {
         Object[] var1 = new Object[]{Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z)};
         return String.format("Acceleration - x: %5.2f, y: %5.2f, z: %5.2f", var1);
      }
   }
}
