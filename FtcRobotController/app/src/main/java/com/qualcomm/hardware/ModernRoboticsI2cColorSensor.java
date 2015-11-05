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
   private final DeviceInterfaceModule interfaceModule;
   private final byte[] readCache;
   private final Lock readCacheLock;
   private final byte[] writeCache;
   private final Lock writeCacheLock;
   private STATE state;
   private volatile int bCurrentLEDState;
   private final int port;

   ModernRoboticsI2cColorSensor(DeviceInterfaceModule interfaceModule, int port) {
      this.state = STATE.READ_MODE;
      this.bCurrentLEDState = 0;
      this.interfaceModule = interfaceModule;
      this.port = port;
      this.readCache = interfaceModule.getI2cReadCache(port);
      this.readCacheLock = interfaceModule.getI2cReadCacheLock(port);
      this.writeCache = interfaceModule.getI2cWriteCache(port);
      this.writeCacheLock = interfaceModule.getI2cWriteCacheLock(port);
      interfaceModule.enableI2cReadMode(port, 60, 3, 6);
      interfaceModule.setI2cPortActionFlag(port);
      interfaceModule.writeI2cCacheToController(port);
      interfaceModule.registerForI2cPortReadyCallback(this, port);
   }

   private int readData(int ib) {
      byte var3;
      try {
         this.readCacheLock.lock();
         var3 = this.readCache[ib];
      } finally {
         this.readCacheLock.unlock();
      }

      return TypeConversion.unsignedByteToInt(var3);
   }

   public int alpha() {
      return this.readData(9);
   }

   public int argb() {
      return Color.argb(this.alpha(), this.red(), this.green(), this.blue());
   }

   public int blue() {
      return this.readData(8);
   }

   public void close() {
   }

   public void enableLed(boolean enable) {
      byte bEnable = 1;
      if(enable) {
         bEnable = 0;
      }
      if(this.bCurrentLEDState != bEnable) {
         this.bCurrentLEDState = bEnable;
         this.state = STATE.WRITE_DIRTY;
         // xyzzy
         try {
            this.writeCacheLock.lock();
            this.writeCache[4] = bEnable;
         } finally {
            this.writeCacheLock.unlock();
         }
      }
   }

   public String getConnectionInfo() {
      return this.interfaceModule.getConnectionInfo() + "; I2C port: " + this.port;
   }

   public String getDeviceName() {
      return "Modern Robotics I2C Color Sensor";
   }

   public int getVersion() {
      return 1;
   }

   public int green() {
      return this.readData(7);
   }

   public void portIsReady(int port) {
      this.interfaceModule.setI2cPortActionFlag(this.port);
      this.interfaceModule.readI2cCacheFromController(this.port);

      if(this.state == STATE.WRITE_DIRTY) {
         this.interfaceModule.enableI2cWriteMode(this.port, 60, 3, 6);
         this.interfaceModule.writeI2cCacheToController(this.port);
         this.state = STATE.WRITE_MODE;

      } else if(this.state == STATE.WRITE_MODE) {
         this.interfaceModule.enableI2cReadMode(this.port, 60, 3, 6);
         this.interfaceModule.writeI2cCacheToController(this.port);
         this.state = STATE.READ_MODE;

      } else {
         this.interfaceModule.writeI2cPortFlagOnlyToController(this.port);
      }
   }

   public int red() {
      return this.readData(6);
   }

   private static enum STATE
      {
         READ_MODE,
         WRITE_DIRTY,
         WRITE_MODE;

      static {
         STATE[] var0 = new STATE[]{READ_MODE, WRITE_DIRTY, WRITE_MODE};
      }
   }
}
