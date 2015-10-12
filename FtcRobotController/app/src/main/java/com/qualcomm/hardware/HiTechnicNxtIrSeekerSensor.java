package com.qualcomm.hardware;

import com.qualcomm.hardware.ModernRoboticsUsbLegacyModule;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.concurrent.locks.Lock;

public class HiTechnicNxtIrSeekerSensor extends IrSeekerSensor implements I2cController.I2cPortReadyCallback {
   public static final double DEFAULT_SIGNAL_DETECTED_THRESHOLD = 0.00390625D;
   public static final byte DIRECTION = 4;
   public static final double[] DIRECTION_TO_ANGLE = new double[]{0.0D, -120.0D, -90.0D, -60.0D, -30.0D, 0.0D, 30.0D, 60.0D, 90.0D, 120.0D};
   public static final int I2C_ADDRESS = 16;
   public static final byte INVALID_ANGLE = 0;
   public static final byte MAX_ANGLE = 9;
   public static final double MAX_SENSOR_STRENGTH = 256.0D;
   public static final int MEM_AC_START_ADDRESS = 73;
   public static final int MEM_DC_START_ADDRESS = 66;
   public static final int MEM_MODE_ADDRESS = 65;
   public static final int MEM_READ_LENGTH = 6;
   public static final byte MIN_ANGLE = 1;
   public static final byte MODE_AC = 0;
   public static final byte MODE_DC = 2;
   public static final byte SENSOR_COUNT = 9;
   public static final byte SENSOR_FIRST = 5;
   private final ModernRoboticsUsbLegacyModule a;
   private final byte[] b;
   private final Lock c;
   private final byte[] d;
   private final Lock e;
   private final int f;
   private IrSeekerSensor.Mode g;
   private double h = 0.00390625D;
   private volatile boolean i;

   public HiTechnicNxtIrSeekerSensor(ModernRoboticsUsbLegacyModule var1, int var2) {
      this.a = var1;
      this.b = var1.getI2cReadCache(var2);
      this.c = var1.getI2cReadCacheLock(var2);
      this.d = var1.getI2cWriteCache(var2);
      this.e = var1.getI2cWriteCacheLock(var2);
      this.f = var2;
      this.g = IrSeekerSensor.Mode.MODE_1200HZ;
      var1.registerForI2cPortReadyCallback(this, var2);
      this.i = true;
   }

   private double a(byte[] var1, int var2) {
      return TypeConversion.unsignedByteToDouble(var1[var2 + 5]) / 256.0D;
   }

   private void a() {
      this.i = true;
      byte var1;
      if(this.g == IrSeekerSensor.Mode.MODE_600HZ) {
         var1 = 2;
      } else {
         var1 = 0;
      }

      this.a.enableI2cWriteMode(this.f, 16, 65, 1);

      try {
         this.e.lock();
         this.d[4] = var1;
      } finally {
         this.e.unlock();
      }

   }

   public void close() {
   }

   public double getAngle() {
      // $FF: Couldn't be decompiled
   }

   public String getConnectionInfo() {
      return this.a.getConnectionInfo() + "; port " + this.f;
   }

   public String getDeviceName() {
      return "NXT IR Seeker Sensor";
   }

   public int getI2cAddress() {
      return 16;
   }

   public IrSeekerSensor.IrSeekerIndividualSensor[] getIndividualSensors() {
      // $FF: Couldn't be decompiled
   }

   public IrSeekerSensor.Mode getMode() {
      return this.g;
   }

   public double getSignalDetectedThreshold() {
      return this.h;
   }

   public double getStrength() {
      // $FF: Couldn't be decompiled
   }

   public int getVersion() {
      return 2;
   }

   public void portIsReady(int var1) {
      this.a.setI2cPortActionFlag(this.f);
      this.a.readI2cCacheFromController(this.f);
      if(this.i) {
         if(this.g == IrSeekerSensor.Mode.MODE_600HZ) {
            this.a.enableI2cReadMode(this.f, 16, 66, 6);
         } else {
            this.a.enableI2cReadMode(this.f, 16, 73, 6);
         }

         this.a.writeI2cCacheToController(this.f);
         this.i = false;
      } else {
         this.a.writeI2cPortFlagOnlyToController(this.f);
      }
   }

   public void setI2cAddress(int var1) {
      throw new UnsupportedOperationException("This method is not supported.");
   }

   public void setMode(IrSeekerSensor.Mode var1) {
      if(this.g != var1) {
         this.g = var1;
         this.a();
      }
   }

   public void setSignalDetectedThreshold(double var1) {
      this.h = var1;
   }

   public boolean signalDetected() {
      boolean var1 = true;
      if(this.i) {
         return false;
      } else {
         boolean var6 = false;

         byte var3;
         try {
            var6 = true;
            this.c.lock();
            var3 = this.b[4];
            var6 = false;
         } finally {
            if(var6) {
               this.c.unlock();
            }
         }

         boolean var4;
         if(var3 != 0) {
            var4 = var1;
         } else {
            var4 = false;
         }

         this.c.unlock();
         if(!var4 || this.getStrength() <= this.h) {
            var1 = false;
         }

         return var1;
      }
   }
}
