package com.qualcomm.hardware;

import android.graphics.Color;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.concurrent.locks.Lock;

public class ModernRoboticsI2cColorSensor extends ColorSensor implements I2cController.I2cPortReadyCallback {
   public static final int ADDRESS_COLOR_NUMBER = 4;
   public static final int ADDRESS_COMMAND = 3;
   public static final int ADDRESS_I2C = 60;
   public static final int BUFFER_LENGTH = 6;
   public static final int COMMAND_ACTIVE_LED = 0;
   public static final int COMMAND_PASSIVE_LED = 1;
   public static final int OFFSET_ALPHA_VALUE = 9;
   public static final int OFFSET_BLUE_READING = 8;
   public static final int OFFSET_COLOR_NUMBER = 5;
   public static final int OFFSET_COMMAND = 4;
   public static final int OFFSET_GREEN_READING = 7;
   public static final int OFFSET_RED_READING = 6;
   private final DeviceInterfaceModule a;
   private final byte[] b;
   private final Lock c;
   private final byte[] d;
   private final Lock e;
   private ModernRoboticsI2cColorSensor.a f;
   private volatile int g;
   private final int h;

   ModernRoboticsI2cColorSensor(DeviceInterfaceModule var1, int var2) {
      this.f = ModernRoboticsI2cColorSensor.a.a;
      this.g = 0;
      this.a = var1;
      this.h = var2;
      this.b = var1.getI2cReadCache(var2);
      this.c = var1.getI2cReadCacheLock(var2);
      this.d = var1.getI2cWriteCache(var2);
      this.e = var1.getI2cWriteCacheLock(var2);
      var1.enableI2cReadMode(var2, 60, 3, 6);
      var1.setI2cPortActionFlag(var2);
      var1.writeI2cCacheToController(var2);
      var1.registerForI2cPortReadyCallback(this, var2);
   }

   private int a(int var1) {
      byte var3;
      try {
         this.c.lock();
         var3 = this.b[var1];
      } finally {
         this.c.unlock();
      }

      return TypeConversion.unsignedByteToInt(var3);
   }

   public int alpha() {
      return this.a(9);
   }

   public int argb() {
      return Color.argb(this.alpha(), this.red(), this.green(), this.blue());
   }

   public int blue() {
      return this.a(8);
   }

   public void close() {
   }

   public void enableLed(boolean var1) {
      byte var2 = 1;
      if(var1) {
         var2 = 0;
      }

      if(this.g != var2) {
         this.g = var2;
         this.f = ModernRoboticsI2cColorSensor.a.b;

         try {
            this.e.lock();
            this.d[4] = var2;
         } finally {
            this.e.unlock();
         }

      }
   }

   public String getConnectionInfo() {
      return this.a.getConnectionInfo() + "; I2C port: " + this.h;
   }

   public String getDeviceName() {
      return "Modern Robotics I2C Color Sensor";
   }

   public int getVersion() {
      return 1;
   }

   public int green() {
      return this.a(7);
   }

   public void portIsReady(int var1) {
      this.a.setI2cPortActionFlag(this.h);
      this.a.readI2cCacheFromController(this.h);
      if(this.f == ModernRoboticsI2cColorSensor.a.b) {
         this.a.enableI2cWriteMode(this.h, 60, 3, 6);
         this.a.writeI2cCacheToController(this.h);
         this.f = ModernRoboticsI2cColorSensor.a.c;
      } else if(this.f == ModernRoboticsI2cColorSensor.a.c) {
         this.a.enableI2cReadMode(this.h, 60, 3, 6);
         this.a.writeI2cCacheToController(this.h);
         this.f = ModernRoboticsI2cColorSensor.a.a;
      } else {
         this.a.writeI2cPortFlagOnlyToController(this.h);
      }
   }

   public int red() {
      return this.a(6);
   }

   private static enum a {
      a,
      b,
      c;

      static {
         ModernRoboticsI2cColorSensor.a[] var0 = new ModernRoboticsI2cColorSensor.a[]{a, b, c};
      }
   }
}
