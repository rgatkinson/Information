package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.TypeConversion;

import java.util.concurrent.locks.Lock;

public class HiTechnicNxtUltrasonicSensor extends UltrasonicSensor implements I2cController.I2cPortReadyCallback {
   public static final int ADDRESS_DISTANCE = 66;
   public static final int I2C_ADDRESS = 2;
   public static final int MAX_PORT = 5;
   public static final int MIN_PORT = 4;
   Lock a;
   byte[] b;
   private final ModernRoboticsUsbLegacyModule c;
   private final int d;

   HiTechnicNxtUltrasonicSensor(ModernRoboticsUsbLegacyModule var1, int var2) {
      this.a(var2);
      this.c = var1;
      this.d = var2;
      this.a = var1.getI2cReadCacheLock(var2);
      this.b = var1.getI2cReadCache(var2);
      var1.enableI2cReadMode(var2, 2, 66, 1);
      var1.enable9v(var2, true);
      var1.setI2cPortActionFlag(var2);
      var1.readI2cCacheFromController(var2);
      var1.registerForI2cPortReadyCallback(this, var2);
   }

   private void a(int var1) {
      if(var1 < 4 || var1 > 5) {
         String var2 = "Port %d is invalid for " + this.getDeviceName() + "; valid ports are %d or %d";
         Object[] var3 = new Object[]{Integer.valueOf(var1), Integer.valueOf(4), Integer.valueOf(5)};
         throw new IllegalArgumentException(String.format(var2, var3));
      }
   }

   public void close() {
   }

   public String getConnectionInfo() {
      return this.c.getConnectionInfo() + "; port " + this.d;
   }

   public String getDeviceName() {
      return "NXT Ultrasonic Sensor";
   }

   public double getUltrasonicLevel() {
      byte var2;
      try {
         this.a.lock();
         var2 = this.b[4];
      } finally {
         this.a.unlock();
      }

      return TypeConversion.unsignedByteToDouble(var2);
   }

   public int getVersion() {
      return 1;
   }

   public void portIsReady(int var1) {
      this.c.setI2cPortActionFlag(this.d);
      this.c.writeI2cCacheToController(this.d);
      this.c.readI2cCacheFromController(this.d);
   }

   public String status() {
      Object[] var1 = new Object[]{this.c.getSerialNumber().toString(), Integer.valueOf(this.d)};
      return String.format("NXT Ultrasonic Sensor, connected via device %s, port %d", var1);
   }
}
