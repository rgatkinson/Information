package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cController;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;

public class ModernRoboticsI2cGyro extends GyroSensor implements HardwareDevice, I2cController.I2cPortReadyCallback {
   public static final int ADDRESS_I2C = 32;
   protected static final int BUFFER_LENGTH = 18;
   protected static final byte COMMAND_NORMAL = 0;
   protected static final byte COMMAND_NULL = 78;
   protected static final byte COMMAND_RESET_Z_AXIS = 82;
   protected static final byte COMMAND_WRITE_EEPROM = 87;
   protected static final int OFFSET_COMMAND = 3;
   protected static final int OFFSET_FIRMWARE_REV = 0;
   protected static final int OFFSET_HEADING_DATA = 4;
   protected static final int OFFSET_INTEGRATED_Z_VAL = 6;
   protected static final int OFFSET_MANUFACTURE_CODE = 1;
   protected static final int OFFSET_NEW_I2C_ADDRESS = 112;
   protected static final int OFFSET_RAW_X_VAL = 8;
   protected static final int OFFSET_RAW_Y_VAL = 10;
   protected static final int OFFSET_RAW_Z_VAL = 12;
   protected static final int OFFSET_SENSOR_ID = 2;
   protected static final int OFFSET_TRIGGER_1 = 113;
   protected static final int OFFSET_TRIGGER_2 = 114;
   protected static final int OFFSET_Z_AXIS_OFFSET = 14;
   protected static final int OFFSET_Z_AXIS_SCALE_COEF = 16;
   protected static final int TRIGGER_1_VAL = 85;
   protected static final int TRIGGER_2_VAL = 170;
   private int a = 32;
   private final DeviceInterfaceModule b;
   private final byte[] c;
   private final Lock d;
   private final byte[] e;
   private final Lock f;
   private final int g;
   private ModernRoboticsI2cGyro.HeadingMode h;
   private ModernRoboticsI2cGyro.MeasurementMode i;
   private ModernRoboticsI2cGyro.a j;
   protected ConcurrentLinkedQueue<ModernRoboticsI2cGyro.GyroI2cTransaction> transactionQueue;

   public ModernRoboticsI2cGyro(DeviceInterfaceModule var1, int var2) {
      this.b = var1;
      this.g = var2;
      this.c = var1.getI2cReadCache(var2);
      this.d = var1.getI2cReadCacheLock(var2);
      this.e = var1.getI2cWriteCache(var2);
      this.f = var1.getI2cWriteCacheLock(var2);
      this.h = ModernRoboticsI2cGyro.HeadingMode.HEADING_CARDINAL;
      var1.enableI2cReadMode(var2, 32, 0, 18);
      var1.setI2cPortActionFlag(var2);
      var1.writeI2cCacheToController(var2);
      var1.registerForI2cPortReadyCallback(this, var2);
      this.transactionQueue = new ConcurrentLinkedQueue();
      this.j = new ModernRoboticsI2cGyro.a(null);
      this.i = ModernRoboticsI2cGyro.MeasurementMode.GYRO_NORMAL;
   }

   private void a() {
      try {
         this.d.lock();
         ByteBuffer var2 = ByteBuffer.wrap(this.c);
         var2.order(ByteOrder.LITTLE_ENDIAN);
         this.j.a = this.c[4];
         this.j.b = this.c[5];
         this.j.c = this.c[6];
         this.j.d = this.c[7];
         this.j.e = var2.getShort(8);
         this.j.f = var2.getShort(10);
         this.j.g = var2.getShort(12);
         this.j.h = var2.getShort(14);
         this.j.i = var2.getShort(16);
         this.j.j = var2.getShort(18);
         this.j.k = var2.getShort(20);
      } finally {
         this.d.unlock();
      }

   }

   private void b() {
      this.queueTransaction(new ModernRoboticsI2cGyro.GyroI2cTransaction());
   }

   protected void buginf(String var1) {
   }

   public void calibrate() {
      this.queueTransaction(new ModernRoboticsI2cGyro.GyroI2cTransaction((byte)78));
   }

   public void close() {
   }

   public String getConnectionInfo() {
      return this.b.getConnectionInfo() + "; I2C port: " + this.g;
   }

   public String getDeviceName() {
      return "Modern Robotics Gyro";
   }

   public int getHeading() {
      return this.h == ModernRoboticsI2cGyro.HeadingMode.HEADING_CARDINAL?(this.j.e == 0?this.j.e:Math.abs(-360 + this.j.e)):this.j.e;
   }

   public ModernRoboticsI2cGyro.HeadingMode getHeadingMode() {
      return this.h;
   }

   public int getIntegratedZValue() {
      return this.j.f;
   }

   public ModernRoboticsI2cGyro.MeasurementMode getMeasurementMode() {
      return this.i;
   }

   public double getRotation() {
      this.notSupported();
      return 0.0D;
   }

   public int getVersion() {
      return 1;
   }

   public boolean isCalibrating() {
      return this.i != ModernRoboticsI2cGyro.MeasurementMode.GYRO_NORMAL;
   }

   public void portIsReady(int param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean queueTransaction(ModernRoboticsI2cGyro.GyroI2cTransaction var1) {
      return this.queueTransaction(var1, false);
   }

   public boolean queueTransaction(ModernRoboticsI2cGyro.GyroI2cTransaction var1, boolean var2) {
      if(!var2) {
         Iterator var4 = this.transactionQueue.iterator();

         while(var4.hasNext()) {
            if(((ModernRoboticsI2cGyro.GyroI2cTransaction)var4.next()).isEqual(var1)) {
               this.buginf("NO Queue transaction " + var1.toString());
               return false;
            }
         }
      }

      this.buginf("YES Queue transaction " + var1.toString());
      this.transactionQueue.add(var1);
      return true;
   }

   public int rawX() {
      return this.j.g;
   }

   public int rawY() {
      return this.j.h;
   }

   public int rawZ() {
      return this.j.i;
   }

   public void resetZAxisIntegrator() {
      this.queueTransaction(new ModernRoboticsI2cGyro.GyroI2cTransaction((byte)82));
   }

   public void setHeadingMode(ModernRoboticsI2cGyro.HeadingMode var1) {
      this.h = var1;
   }

   public String status() {
      Object[] var1 = new Object[]{this.b.getSerialNumber().toString(), Integer.valueOf(this.g)};
      return String.format("Modern Robotics Gyro, connected via device %s, port %d", var1);
   }

   public class GyroI2cTransaction {
      ModernRoboticsI2cGyro.I2cTransactionState a;
      byte[] b;
      byte c;
      byte d;
      boolean e;

      public GyroI2cTransaction() {
         this.c = 0;
         this.d = 18;
         this.e = false;
      }

      public GyroI2cTransaction(byte var2) {
         this.c = 3;
         this.b = new byte[1];
         this.b[0] = var2;
         this.d = (byte)this.b.length;
         this.e = true;
      }

      public boolean isEqual(ModernRoboticsI2cGyro.GyroI2cTransaction var1) {
         if(this.c == var1.c) {
            switch(this.c) {
            case 3:
            case 16:
               if(Arrays.equals(this.b, var1.b)) {
                  return true;
               }
               break;
            default:
               return false;
            }
         }

         return false;
      }
   }

   public static enum HeadingMode {
      HEADING_CARDINAL,
      HEADING_CARTESIAN;

      static {
         ModernRoboticsI2cGyro.HeadingMode[] var0 = new ModernRoboticsI2cGyro.HeadingMode[]{HEADING_CARTESIAN, HEADING_CARDINAL};
      }
   }

   protected static enum I2cTransactionState {
      DONE,
      PENDING_I2C_READ,
      PENDING_I2C_WRITE,
      PENDING_READ_DONE,
      QUEUED;

      static {
         ModernRoboticsI2cGyro.I2cTransactionState[] var0 = new ModernRoboticsI2cGyro.I2cTransactionState[]{QUEUED, PENDING_I2C_READ, PENDING_I2C_WRITE, PENDING_READ_DONE, DONE};
      }
   }

   public static enum MeasurementMode {
      GYRO_CALIBRATING,
      GYRO_NORMAL;

      static {
         ModernRoboticsI2cGyro.MeasurementMode[] var0 = new ModernRoboticsI2cGyro.MeasurementMode[]{GYRO_CALIBRATING, GYRO_NORMAL};
      }
   }

   private class a {
      byte a;
      byte b;
      byte c;
      byte d;
      short e;
      short f;
      short g;
      short h;
      short i;
      short j;
      short k;

      private a() {
      }

      // $FF: synthetic method
      a(Object var2) {
         this();
      }
   }
}
