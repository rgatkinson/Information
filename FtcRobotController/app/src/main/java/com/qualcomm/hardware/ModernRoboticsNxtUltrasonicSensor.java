package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.concurrent.locks.Lock;

public class ModernRoboticsNxtUltrasonicSensor extends UltrasonicSensor implements I2cController.I2cPortReadyCallback {
   public static final int ADDRESS_DISTANCE = 66;
   public static final int I2C_ADDRESS = 2;
   Lock readCacheLock;
   byte[] readCache;
   private final ModernRoboticsUsbLegacyModule legacyModule;
   private final int port;

   ModernRoboticsNxtUltrasonicSensor(ModernRoboticsUsbLegacyModule legacyModule, int port) {
      this.legacyModule = legacyModule;
      this.port = port;
      this.readCacheLock = legacyModule.getI2cReadCacheLock(port);
      this.readCache = legacyModule.getI2cReadCache(port);
      legacyModule.enableI2cReadMode(port, I2C_ADDRESS, ADDRESS_DISTANCE, 1);
      legacyModule.enable9v(port, true);
      legacyModule.setI2cPortActionFlag(port);
      legacyModule.readI2cCacheFromController(port);
      legacyModule.registerForI2cPortReadyCallback(this, port);
   }

   public void close() {
   }

   public String getConnectionInfo() {
      return this.legacyModule.getConnectionInfo() + "; port " + this.port;
   }

   public String getDeviceName() {
      return "NXT Ultrasonic Sensor";
   }

   public double getUltrasonicLevel() {
      byte resultData;
      try {
         this.readCacheLock.lock();
         resultData = this.readCache[4];
      } finally {
         this.readCacheLock.unlock();
      }

      return TypeConversion.unsignedByteToDouble(resultData);
   }

   public int getVersion() {
      return 1;
   }

   public void portIsReady(int port) {
      this.legacyModule.setI2cPortActionFlag(this.port);
      this.legacyModule.writeI2cCacheToController(this.port);
      this.legacyModule.readI2cCacheFromController(this.port);
   }

   public String status() {
      return String.format("NXT Ultrasonic Sensor, connected via device %s, port %d", this.legacyModule.getSerialNumber().toString(), this.port);
   }
}
