package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.HardwareDevice;

public abstract class IrSeekerSensor implements HardwareDevice {
   public static final int MAX_NEW_I2C_ADDRESS = 126;
   public static final int MIN_NEW_I2C_ADDRESS = 16;

   public static void throwIfModernRoboticsI2cAddressIsInvalid(int var0) {
      if(var0 >= 16 && var0 <= 126) {
         if(var0 % 2 != 0) {
            Object[] var2 = new Object[]{Integer.valueOf(var0)};
            throw new IllegalArgumentException(String.format("New I2C address %d is invalid; the address must be even.", var2));
         }
      } else {
         Object[] var1 = new Object[]{Integer.valueOf(var0), Integer.valueOf(16), Integer.valueOf(126)};
         throw new IllegalArgumentException(String.format("New I2C address %d is invalid; valid range is: %d..%d", var1));
      }
   }

   public abstract double getAngle();

   public abstract int getI2cAddress();

   public abstract IrSeekerSensor.IrSeekerIndividualSensor[] getIndividualSensors();

   public abstract IrSeekerSensor.Mode getMode();

   public abstract double getSignalDetectedThreshold();

   public abstract double getStrength();

   public abstract void setI2cAddress(int var1);

   public abstract void setMode(IrSeekerSensor.Mode var1);

   public abstract void setSignalDetectedThreshold(double var1);

   public abstract boolean signalDetected();

   public String toString() {
      if(this.signalDetected()) {
         Object[] var1 = new Object[]{Double.valueOf(100.0D * this.getStrength()), Double.valueOf(this.getAngle())};
         return String.format("IR Seeker: %3.0f%% signal at %6.1f degrees", var1);
      } else {
         return "IR Seeker:  --% signal at  ---.- degrees";
      }
   }

   public static class IrSeekerIndividualSensor {
      private double a;
      private double b;

      public IrSeekerIndividualSensor() {
         this(0.0D, 0.0D);
      }

      public IrSeekerIndividualSensor(double var1, double var3) {
         this.a = 0.0D;
         this.b = 0.0D;
         this.a = var1;
         this.b = var3;
      }

      public double getSensorAngle() {
         return this.a;
      }

      public double getSensorStrength() {
         return this.b;
      }

      public String toString() {
         Object[] var1 = new Object[]{Double.valueOf(this.a), Double.valueOf(100.0D * this.b)};
         return String.format("IR Sensor: %3.1f degrees at %3.1f%% power", var1);
      }
   }

   public static enum Mode {
      MODE_1200HZ,
      MODE_600HZ;

      static {
         IrSeekerSensor.Mode[] var0 = new IrSeekerSensor.Mode[]{MODE_600HZ, MODE_1200HZ};
      }
   }
}
