package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.util.TypeConversion;

import java.nio.ByteOrder;
import java.util.concurrent.locks.Lock;

public class ModernRoboticsI2cIrSeekerSensorV3 extends IrSeekerSensor implements I2cController.I2cPortReadyCallback {
   public static final int ADDRESS_MEM_START = 4;
   public static final double DEFAULT_SIGNAL_DETECTED_THRESHOLD = 0.00390625D;
   public static final byte INVALID_ANGLE = 0;
   public static final double MAX_SENSOR_STRENGTH = 256.0D;
   public static final int MEM_LENGTH = 12;
   public static final int OFFSET_1200HZ_HEADING_DATA = 4;
   public static final int OFFSET_1200HZ_LEFT_SIDE_RAW_DATA = 8;
   public static final int OFFSET_1200HZ_RIGHT_SIDE_RAW_DATA = 10;
   public static final int OFFSET_1200HZ_SIGNAL_STRENGTH = 5;
   public static final int OFFSET_600HZ_HEADING_DATA = 6;
   public static final int OFFSET_600HZ_LEFT_SIDE_RAW_DATA = 12;
   public static final int OFFSET_600HZ_RIGHT_SIDE_RAW_DATA = 14;
   public static final int OFFSET_600HZ_SIGNAL_STRENGTH = 7;
   public static final byte SENSOR_COUNT = 2;
   private final DeviceInterfaceModule a;
   private final int b;
   private final byte[] d;
   private final Lock e;
   public volatile int I2C_ADDRESS = 56;
   private IrSeekerSensor.Mode c;
   private double f = 0.00390625D;

   public ModernRoboticsI2cIrSeekerSensorV3(DeviceInterfaceModule var1, int var2) {
      this.a = var1;
      this.b = var2;
      this.c = IrSeekerSensor.Mode.MODE_1200HZ;
      this.d = this.a.getI2cReadCache(var2);
      this.e = this.a.getI2cReadCacheLock(var2);
      this.a.enableI2cReadMode(var2, this.I2C_ADDRESS, 4, 12);
      this.a.setI2cPortActionFlag(var2);
      this.a.writeI2cCacheToController(var2);
      this.a.registerForI2cPortReadyCallback(this, var2);
   }

   public void close() {
   }

   public double getAngle() {
      byte var1;
      if(this.c == IrSeekerSensor.Mode.MODE_1200HZ) {
         var1 = 4;
      } else {
         var1 = 6;
      }

      boolean var7 = false;

      byte var3;
      try {
         var7 = true;
         this.e.lock();
         var3 = this.d[var1];
         var7 = false;
      } finally {
         if(var7) {
            this.e.unlock();
         }
      }

      double var4 = (double)var3;
      this.e.unlock();
      return var4;
   }

   public String getConnectionInfo() {
      return this.a.getConnectionInfo() + "; I2C port " + this.b;
   }

   public String getDeviceName() {
      return "Modern Robotics I2C IR Seeker Sensor";
   }

   public int getI2cAddress() {
      return this.I2C_ADDRESS;
   }

   public void setI2cAddress(int var1) {
      IrSeekerSensor.throwIfModernRoboticsI2cAddressIsInvalid(var1);
      this.I2C_ADDRESS = var1;
   }

   public IrSeekerIndividualSensor[] getIndividualSensors() {
      IrSeekerIndividualSensor[] var1 = new IrSeekerIndividualSensor[2];

      try {
         this.e.lock();
         int var2 = this.c == Mode.MODE_1200HZ ? 8 : 12;
         byte[] var3 = new byte[2];
         System.arraycopy(this.d, var2, var3, 0, var3.length);
         double var4 = (double) TypeConversion.byteArrayToShort(var3, ByteOrder.LITTLE_ENDIAN) / 256.0D;
         var1[0] = new IrSeekerIndividualSensor(-1.0D, var4);
         int var6 = this.c == Mode.MODE_1200HZ ? 10 : 14;
         byte[] var7 = new byte[2];
         System.arraycopy(this.d, var6, var7, 0, var7.length);
         double var8 = (double) TypeConversion.byteArrayToShort(var7, ByteOrder.LITTLE_ENDIAN) / 256.0D;
         var1[1] = new IrSeekerIndividualSensor(1.0D, var8);
      } finally {
         this.e.unlock();
      }

      return var1;
   }

   public IrSeekerSensor.Mode getMode() {
      return this.c;
   }

   public void setMode(IrSeekerSensor.Mode var1) {
      this.c = var1;
   }

   public double getSignalDetectedThreshold() {
      return this.f;
   }

   public void setSignalDetectedThreshold(double var1) {
      this.f = var1;
   }

   public double getStrength() {
      byte var1;
      if(this.c == IrSeekerSensor.Mode.MODE_1200HZ) {
         var1 = 5;
      } else {
         var1 = 7;
      }

      boolean var8 = false;

      double var3;
      try {
         var8 = true;
         this.e.lock();
         var3 = TypeConversion.unsignedByteToDouble(this.d[var1]);
         var8 = false;
      } finally {
         if(var8) {
            this.e.unlock();
         }
      }

      double var5 = var3 / 256.0D;
      this.e.unlock();
      return var5;
   }

   public int getVersion() {
      return 3;
   }

   public void portIsReady(int var1) {
      this.a.setI2cPortActionFlag(var1);
      this.a.readI2cCacheFromController(var1);
      this.a.writeI2cPortFlagOnlyToController(var1);
   }

   public boolean signalDetected() {
      return this.getStrength() > this.f;
   }
}
