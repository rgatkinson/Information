package com.qualcomm.hardware.hardware;

import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.I2cController;

import java.util.concurrent.locks.Lock;

public class HiTechnicNxtAccelerationSensor extends AccelerationSensor implements I2cController.I2cPortReadyCallback {
   public static final int ACCEL_LENGTH = 6;
   public static final int ADDRESS_ACCEL_START = 66;
   public static final byte I2C_ADDRESS = 2;
   private final ModernRoboticsUsbLegacyModule a;
   private final byte[] b;
   private final Lock c;
   private final int d;

   public HiTechnicNxtAccelerationSensor(ModernRoboticsUsbLegacyModule var1, int var2) {
      var1.enableI2cReadMode(var2, 2, 66, 6);
      this.a = var1;
      this.b = var1.getI2cReadCache(var2);
      this.c = var1.getI2cReadCacheLock(var2);
      this.d = var2;
      var1.registerForI2cPortReadyCallback(this, var2);
   }

   private double a(double var1, double var3) {
      return (var3 + 4.0D * var1) / 200.0D;
   }

   public void close() {
   }

   public AccelerationSensor.Acceleration getAcceleration() {
      AccelerationSensor.Acceleration var1 = new AccelerationSensor.Acceleration();

      try {
         this.c.lock();
         var1.x = this.a((double)this.b[4], (double)this.b[7]);
         var1.y = this.a((double)this.b[5], (double)this.b[8]);
         var1.z = this.a((double)this.b[6], (double)this.b[9]);
      } finally {
         this.c.unlock();
      }

      return var1;
   }

   public String getConnectionInfo() {
      return this.a.getConnectionInfo() + "; port " + this.d;
   }

   public String getDeviceName() {
      return "NXT Acceleration Sensor";
   }

   public int getVersion() {
      return 1;
   }

   public void portIsReady(int var1) {
      this.a.setI2cPortActionFlag(this.d);
      this.a.writeI2cPortFlagOnlyToController(this.d);
      this.a.readI2cCacheFromController(this.d);
   }

   public String status() {
      Object[] var1 = new Object[]{this.a.getSerialNumber().toString(), Integer.valueOf(this.d)};
      return String.format("NXT Acceleration Sensor, connected via device %s, port %d", var1);
   }
}
