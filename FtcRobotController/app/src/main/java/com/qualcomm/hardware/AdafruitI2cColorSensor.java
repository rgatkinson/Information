package com.qualcomm.hardware;

import android.graphics.Color;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cController;
import java.util.concurrent.locks.Lock;

public class AdafruitI2cColorSensor extends ColorSensor implements I2cController.I2cPortReadyCallback {
   public static final int ADDRESS_TCS34725_ENABLE = 0;
   public static final int I2C_ADDRESS_TCS34725 = 82;
   public static final int OFFSET_ALPHA_HIGH_BYTE = 5;
   public static final int OFFSET_ALPHA_LOW_BYTE = 4;
   public static final int OFFSET_BLUE_HIGH_BYTE = 11;
   public static final int OFFSET_BLUE_LOW_BYTE = 10;
   public static final int OFFSET_GREEN_HIGH_BYTE = 9;
   public static final int OFFSET_GREEN_LOW_BYTE = 8;
   public static final int OFFSET_RED_HIGH_BYTE = 7;
   public static final int OFFSET_RED_LOW_BYTE = 6;
   public static final int TCS34725_BDATAL = 26;
   public static final int TCS34725_CDATAL = 20;
   public static final int TCS34725_COMMAND_BIT = 128;
   public static final int TCS34725_ENABLE_AEN = 2;
   public static final int TCS34725_ENABLE_AIEN = 16;
   public static final int TCS34725_ENABLE_PON = 1;
   public static final int TCS34725_GDATAL = 24;
   public static final int TCS34725_ID = 18;
   public static final int TCS34725_RDATAL = 22;
   private final DeviceInterfaceModule a;
   private final byte[] b;
   private final Lock c;
   private final byte[] d;
   private final Lock e;
   private final int f;
   private boolean g = false;
   private boolean h = false;

   public AdafruitI2cColorSensor(DeviceInterfaceModule var1, int var2) {
      this.f = var2;
      this.a = var1;
      this.b = var1.getI2cReadCache(var2);
      this.c = var1.getI2cReadCacheLock(var2);
      this.d = var1.getI2cWriteCache(var2);
      this.e = var1.getI2cWriteCacheLock(var2);
      this.g = true;
      var1.registerForI2cPortReadyCallback(this, var2);
   }

   private int a(int var1, int var2) {
      boolean var8 = false;

      int var4;
      byte var5;
      try {
         var8 = true;
         this.c.lock();
         var4 = this.b[var1] << 8;
         var5 = this.b[var2];
         var8 = false;
      } finally {
         if(var8) {
            this.c.unlock();
         }
      }

      int var6 = var4 | var5 & 255;
      this.c.unlock();
      return var6;
   }

   private void a() {
      this.a.enableI2cReadMode(this.f, 82, 148, 8);
      this.a.writeI2cCacheToController(this.f);
   }

   private void b() {
      this.a.enableI2cWriteMode(this.f, 82, 128, 1);

      try {
         this.e.lock();
         this.d[4] = 3;
      } finally {
         this.e.unlock();
      }

      this.a.setI2cPortActionFlag(this.f);
      this.a.writeI2cCacheToController(this.f);
   }

   public int alpha() {
      return this.a(5, 4);
   }

   public int argb() {
      return Color.argb(this.alpha(), this.red(), this.green(), this.blue());
   }

   public int blue() {
      return this.a(11, 10);
   }

   public void close() {
   }

   public void enableLed(boolean var1) {
      throw new UnsupportedOperationException("enableLed is not implemented.");
   }

   public String getConnectionInfo() {
      return this.a.getConnectionInfo() + "; I2C port: " + this.f;
   }

   public String getDeviceName() {
      return "Adafruit I2C Color Sensor";
   }

   public int getI2cAddress() {
      throw new UnsupportedOperationException("getI2cAddress is not supported.");
   }

   public int getVersion() {
      return 1;
   }

   public int green() {
      return this.a(9, 8);
   }

   public void portIsReady(int var1) {
      if(this.g) {
         this.b();
         this.g = false;
         this.h = true;
      } else if(this.h) {
         this.a();
         this.h = false;
      }

      this.a.readI2cCacheFromController(this.f);
      this.a.setI2cPortActionFlag(this.f);
      this.a.writeI2cPortFlagOnlyToController(this.f);
   }

   public int red() {
      return this.a(7, 6);
   }

   public void setI2cAddress(int var1) {
      throw new UnsupportedOperationException("setI2cAddress is not supported.");
   }
}
