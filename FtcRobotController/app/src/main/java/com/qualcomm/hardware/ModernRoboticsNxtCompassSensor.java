package com.qualcomm.hardware;

import com.qualcomm.hardware.ModernRoboticsUsbLegacyModule;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;

public class ModernRoboticsNxtCompassSensor extends CompassSensor implements I2cController.I2cPortReadyCallback {
   public static final byte CALIBRATION = 67;
   public static final byte CALIBRATION_FAILURE = 70;
   public static final int COMPASS_BUFFER = 65;
   public static final int COMPASS_BUFFER_SIZE = 5;
   public static final byte DIRECTION_END = 9;
   public static final byte DIRECTION_START = 7;
   public static final byte HEADING_IN_TWO_DEGREE_INCREMENTS = 66;
   public static final int HEADING_WORD_LENGTH = 2;
   public static final byte I2C_ADDRESS = 2;
   public static final double INVALID_DIRECTION = -1.0D;
   public static final byte MEASUREMENT = 0;
   public static final byte MODE_CONTROL_ADDRESS = 65;
   public static final byte ONE_DEGREE_HEADING_ADDER = 67;
   private final ModernRoboticsUsbLegacyModule a;
   private final byte[] b;
   private final Lock c;
   private final byte[] d;
   private final Lock e;
   private final int f;
   private CompassSensor.CompassMode g;
   private boolean h;
   private boolean i;

   public ModernRoboticsNxtCompassSensor(ModernRoboticsUsbLegacyModule var1, int var2) {
      this.g = CompassSensor.CompassMode.MEASUREMENT_MODE;
      this.h = false;
      this.i = false;
      var1.enableI2cReadMode(var2, 2, 65, 5);
      this.a = var1;
      this.b = var1.getI2cReadCache(var2);
      this.c = var1.getI2cReadCacheLock(var2);
      this.d = var1.getI2cWriteCache(var2);
      this.e = var1.getI2cWriteCacheLock(var2);
      this.f = var2;
      var1.registerForI2cPortReadyCallback(this, var2);
   }

   private void a() {
      this.h = true;
      byte var1;
      if(this.g == CompassSensor.CompassMode.CALIBRATION_MODE) {
         var1 = 67;
      } else {
         var1 = 0;
      }

      this.a.enableI2cWriteMode(this.f, 2, 65, 1);

      try {
         this.e.lock();
         this.d[3] = var1;
      } finally {
         this.e.unlock();
      }

   }

   private void b() {
      if(this.g == CompassSensor.CompassMode.MEASUREMENT_MODE) {
         this.a.enableI2cReadMode(this.f, 2, 65, 5);
      }

      this.h = false;
   }

   public boolean calibrationFailed() {
      if(this.g != CompassSensor.CompassMode.CALIBRATION_MODE && !this.h) {
         boolean var5 = false;

         byte var2;
         try {
            var5 = true;
            this.c.lock();
            var2 = this.b[3];
            var5 = false;
         } finally {
            if(var5) {
               this.c.unlock();
            }
         }

         boolean var3 = false;
         if(var2 == 70) {
            var3 = true;
         }

         this.c.unlock();
         return var3;
      } else {
         return false;
      }
   }

   public void close() {
   }

   public String getConnectionInfo() {
      return this.a.getConnectionInfo() + "; port " + this.f;
   }

   public String getDeviceName() {
      return "NXT Compass Sensor";
   }

   public double getDirection() {
      if(!this.h && this.g != CompassSensor.CompassMode.CALIBRATION_MODE) {
         byte[] var2;
         try {
            this.c.lock();
            var2 = Arrays.copyOfRange(this.b, 7, 9);
         } finally {
            this.c.unlock();
         }

         return (double)TypeConversion.byteArrayToShort(var2, ByteOrder.LITTLE_ENDIAN);
      } else {
         return -1.0D;
      }
   }

   public int getVersion() {
      return 1;
   }

   public void portIsReady(int var1) {
      this.a.setI2cPortActionFlag(this.f);
      this.a.readI2cCacheFromController(this.f);
      if(this.h) {
         this.b();
         this.a.writeI2cCacheToController(this.f);
      } else {
         this.a.writeI2cPortFlagOnlyToController(this.f);
      }
   }

   public void setMode(CompassSensor.CompassMode var1) {
      if(this.g != var1) {
         this.g = var1;
         this.a();
      }
   }

   public String status() {
      Object[] var1 = new Object[]{this.a.getSerialNumber().toString(), Integer.valueOf(this.f)};
      return String.format("NXT Compass Sensor, connected via device %s, port %d", var1);
   }
}
