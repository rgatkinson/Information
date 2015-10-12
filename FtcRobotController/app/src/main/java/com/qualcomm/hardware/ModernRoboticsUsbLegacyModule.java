package com.qualcomm.hardware;

import com.qualcomm.hardware.ModernRoboticsUsbDevice;
import com.qualcomm.hardware.ReadWriteRunnableSegment;
import com.qualcomm.hardware.ReadWriteRunnableStandard;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.concurrent.locks.Lock;

public class ModernRoboticsUsbLegacyModule extends ModernRoboticsUsbDevice implements LegacyModule {
   public static final int[] ADDRESS_ANALOG_PORT_MAP = new int[]{4, 6, 8, 10, 12, 14};
   public static final int ADDRESS_ANALOG_PORT_S0 = 4;
   public static final int ADDRESS_ANALOG_PORT_S1 = 6;
   public static final int ADDRESS_ANALOG_PORT_S2 = 8;
   public static final int ADDRESS_ANALOG_PORT_S3 = 10;
   public static final int ADDRESS_ANALOG_PORT_S4 = 12;
   public static final int ADDRESS_ANALOG_PORT_S5 = 14;
   public static final int ADDRESS_BUFFER_STATUS = 3;
   public static final int[] ADDRESS_I2C_PORT_MAP = new int[]{16, 48, 80, 112, 144, 176};
   public static final int ADDRESS_I2C_PORT_S1 = 48;
   public static final int ADDRESS_I2C_PORT_S2 = 80;
   public static final int ADDRESS_I2C_PORT_S3 = 112;
   public static final int ADDRESS_I2C_PORT_S4 = 144;
   public static final int ADDRESS_I2C_PORT_S5 = 176;
   public static final int ADDRESS_I2C_PORT_SO = 16;
   public static final int[] BUFFER_FLAG_MAP = new int[]{1, 2, 4, 8, 16, 32};
   public static final byte BUFFER_FLAG_S0 = 1;
   public static final byte BUFFER_FLAG_S1 = 2;
   public static final byte BUFFER_FLAG_S2 = 4;
   public static final byte BUFFER_FLAG_S3 = 8;
   public static final byte BUFFER_FLAG_S4 = 16;
   public static final byte BUFFER_FLAG_S5 = 32;
   public static final boolean DEBUG_LOGGING = false;
   public static final int[] DIGITAL_LINE = new int[]{4, 8};
   public static final byte I2C_ACTION_FLAG = -1;
   public static final byte I2C_NO_ACTION_FLAG = 0;
   public static final byte MAX_PORT_NUMBER = 5;
   public static final byte MIN_PORT_NUMBER = 0;
   public static final int MONITOR_LENGTH = 13;
   public static final byte NUMBER_OF_PORTS = 6;
   public static final byte NXT_MODE_9V_ENABLED = 2;
   public static final byte NXT_MODE_ANALOG = 0;
   public static final byte NXT_MODE_DIGITAL_0 = 4;
   public static final byte NXT_MODE_DIGITAL_1 = 8;
   public static final byte NXT_MODE_I2C = 1;
   public static final byte NXT_MODE_READ = -128;
   public static final byte NXT_MODE_WRITE = 0;
   public static final byte OFFSET_I2C_PORT_FLAG = 31;
   public static final byte OFFSET_I2C_PORT_I2C_ADDRESS = 1;
   public static final byte OFFSET_I2C_PORT_MEMORY_ADDRESS = 2;
   public static final byte OFFSET_I2C_PORT_MEMORY_BUFFER = 4;
   public static final byte OFFSET_I2C_PORT_MEMORY_LENGTH = 3;
   public static final byte OFFSET_I2C_PORT_MODE = 0;
   public static final int[] PORT_9V_CAPABLE = new int[]{4, 5};
   public static final byte SIZE_ANALOG_BUFFER = 2;
   public static final byte SIZE_I2C_BUFFER = 27;
   public static final byte SIZE_OF_PORT_BUFFER = 32;
   public static final byte START_ADDRESS = 3;
   private final ReadWriteRunnableSegment[] a;
   private final I2cController.I2cPortReadyCallback[] b;

   protected ModernRoboticsUsbLegacyModule(SerialNumber var1, RobotUsbDevice var2, EventLoopManager var3) throws RobotCoreException, InterruptedException {
      int var4 = 0;
      super(var1, var3, new ReadWriteRunnableStandard(var1, var2, 13, 3, false));
      this.a = new ReadWriteRunnableSegment[12];
      this.b = new I2cController.I2cPortReadyCallback[6];
      this.readWriteRunnable.setCallback(this);

      while(var4 < 6) {
         this.a[var4] = this.readWriteRunnable.createSegment(var4, ADDRESS_I2C_PORT_MAP[var4], 32);
         this.a[var4 + 6] = this.readWriteRunnable.createSegment(var4 + 6, 31 + ADDRESS_I2C_PORT_MAP[var4], 1);
         this.enableAnalogReadMode(var4);
         this.readWriteRunnable.queueSegmentWrite(var4);
         ++var4;
      }

   }

   private void a(int var1) {
      if(var1 < 0 || var1 > 5) {
         Object[] var2 = new Object[]{Integer.valueOf(var1), Byte.valueOf((byte)0), Byte.valueOf((byte)5)};
         throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are %d..%d", var2));
      }
   }

   private boolean a(int var1, byte var2) {
      return (var2 & BUFFER_FLAG_MAP[var1]) == 0;
   }

   private void b(int var1) {
      if(var1 < 0 || var1 > 27) {
         Object[] var2 = new Object[]{Integer.valueOf(var1), Byte.valueOf((byte)27)};
         throw new IllegalArgumentException(String.format("buffer length of %d is invalid; max value is %d", var2));
      }
   }

   private void c(int var1) {
      if(var1 != 0 && var1 != 1) {
         throw new IllegalArgumentException("line is invalid, valid lines are 0 and 1");
      }
   }

   public void close() {
      super.close();
   }

   public void copyBufferIntoWriteBuffer(int var1, byte[] var2) {
      this.a(var1);
      this.b(var2.length);

      try {
         this.a[var1].getWriteLock().lock();
         System.arraycopy(var2, 0, this.a[var1].getWriteBuffer(), 4, var2.length);
      } finally {
         this.a[var1].getWriteLock().unlock();
      }

   }

   public void deregisterForPortReadyCallback(int var1) {
      this.b[var1] = null;
   }

   public void enable9v(int param1, boolean param2) {
      // $FF: Couldn't be decompiled
   }

   public void enableAnalogReadMode(int var1) {
      this.a(var1);

      try {
         this.a[var1].getWriteLock().lock();
         this.a[var1].getWriteBuffer()[0] = 0;
      } finally {
         this.a[var1].getWriteLock().unlock();
      }

      this.writeI2cCacheToController(var1);
   }

   public void enableI2cReadMode(int var1, int var2, int var3, int var4) {
      this.a(var1);
      this.b(var4);

      try {
         this.a[var1].getWriteLock().lock();
         byte[] var6 = this.a[var1].getWriteBuffer();
         var6[0] = -127;
         var6[1] = (byte)var2;
         var6[2] = (byte)var3;
         var6[3] = (byte)var4;
      } finally {
         this.a[var1].getWriteLock().unlock();
      }

   }

   public void enableI2cWriteMode(int var1, int var2, int var3, int var4) {
      this.a(var1);
      this.b(var4);

      try {
         this.a[var1].getWriteLock().lock();
         byte[] var6 = this.a[var1].getWriteBuffer();
         var6[0] = 1;
         var6[1] = (byte)var2;
         var6[2] = (byte)var3;
         var6[3] = (byte)var4;
      } finally {
         this.a[var1].getWriteLock().unlock();
      }

   }

   public String getConnectionInfo() {
      return "USB " + this.getSerialNumber();
   }

   public byte[] getCopyOfReadBuffer(int var1) {
      this.a(var1);

      byte[] var4;
      try {
         this.a[var1].getReadLock().lock();
         byte[] var3 = this.a[var1].getReadBuffer();
         var4 = new byte[var3[3]];
         System.arraycopy(var3, 4, var4, 0, var4.length);
      } finally {
         this.a[var1].getReadLock().unlock();
      }

      return var4;
   }

   public byte[] getCopyOfWriteBuffer(int var1) {
      this.a(var1);

      byte[] var4;
      try {
         this.a[var1].getWriteLock().lock();
         byte[] var3 = this.a[var1].getWriteBuffer();
         var4 = new byte[var3[3]];
         System.arraycopy(var3, 4, var4, 0, var4.length);
      } finally {
         this.a[var1].getWriteLock().unlock();
      }

      return var4;
   }

   public String getDeviceName() {
      return "Modern Robotics USB Legacy Module";
   }

   public byte[] getI2cReadCache(int var1) {
      this.a(var1);
      return this.a[var1].getReadBuffer();
   }

   public Lock getI2cReadCacheLock(int var1) {
      this.a(var1);
      return this.a[var1].getReadLock();
   }

   public byte[] getI2cWriteCache(int var1) {
      this.a(var1);
      return this.a[var1].getWriteBuffer();
   }

   public Lock getI2cWriteCacheLock(int var1) {
      this.a(var1);
      return this.a[var1].getWriteLock();
   }

   public boolean isI2cPortActionFlagSet(int var1) {
      this.a(var1);
      boolean var6 = false;

      byte var3;
      try {
         var6 = true;
         this.a[var1].getReadLock().lock();
         var3 = this.a[var1].getReadBuffer()[31];
         var6 = false;
      } finally {
         if(var6) {
            this.a[var1].getReadLock().unlock();
         }
      }

      boolean var4;
      if(var3 == -1) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.a[var1].getReadLock().unlock();
      return var4;
   }

   public boolean isI2cPortInReadMode(int var1) {
      this.a(var1);
      boolean var6 = false;

      byte var3;
      try {
         var6 = true;
         this.a[var1].getReadLock().lock();
         var3 = this.a[var1].getReadBuffer()[0];
         var6 = false;
      } finally {
         if(var6) {
            this.a[var1].getReadLock().unlock();
         }
      }

      boolean var4 = false;
      if(var3 == -127) {
         var4 = true;
      }

      this.a[var1].getReadLock().unlock();
      return var4;
   }

   public boolean isI2cPortInWriteMode(int var1) {
      byte var2 = 1;
      this.a(var1);
      boolean var6 = false;

      byte var4;
      try {
         var6 = true;
         this.a[var1].getReadLock().lock();
         var4 = this.a[var1].getReadBuffer()[0];
         var6 = false;
      } finally {
         if(var6) {
            this.a[var1].getReadLock().unlock();
         }
      }

      if(var4 != var2) {
         var2 = 0;
      }

      this.a[var1].getReadLock().unlock();
      return (boolean)var2;
   }

   public boolean isI2cPortReady(int var1) {
      return this.a(var1, this.read(3));
   }

   public byte[] readAnalog(int var1) {
      this.a(var1);
      return this.read(ADDRESS_ANALOG_PORT_MAP[var1], 2);
   }

   public void readComplete() throws InterruptedException {
      if(this.b != null) {
         byte var1 = this.read(3);

         for(int var2 = 0; var2 < 6; ++var2) {
            if(this.b[var2] != null && this.a(var2, var1)) {
               this.b[var2].portIsReady(var2);
            }
         }
      }

   }

   public void readI2cCacheFromController(int var1) {
      this.a(var1);
      this.readWriteRunnable.queueSegmentRead(var1);
   }

   @Deprecated
   public void readI2cCacheFromModule(int var1) {
      this.readI2cCacheFromController(var1);
   }

   public void registerForI2cPortReadyCallback(I2cController.I2cPortReadyCallback var1, int var2) {
      this.b[var2] = var1;
   }

   public void setData(int var1, byte[] var2, int var3) {
      this.a(var1);
      this.b(var3);

      try {
         this.a[var1].getWriteLock().lock();
         byte[] var5 = this.a[var1].getWriteBuffer();
         System.arraycopy(var2, 0, var5, 4, var3);
         var5[3] = (byte)var3;
      } finally {
         this.a[var1].getWriteLock().unlock();
      }

   }

   public void setDigitalLine(int param1, int param2, boolean param3) {
      // $FF: Couldn't be decompiled
   }

   public void setI2cPortActionFlag(int var1) {
      this.a(var1);

      try {
         this.a[var1].getWriteLock().lock();
         this.a[var1].getWriteBuffer()[31] = -1;
      } finally {
         this.a[var1].getWriteLock().unlock();
      }

   }

   public void setReadMode(int var1, int var2, int var3, int var4) {
      this.a(var1);

      try {
         this.a[var1].getWriteLock().lock();
         byte[] var6 = this.a[var1].getWriteBuffer();
         var6[0] = -127;
         var6[1] = (byte)var2;
         var6[2] = (byte)var3;
         var6[3] = (byte)var4;
      } finally {
         this.a[var1].getWriteLock().unlock();
      }

   }

   public void setWriteMode(int var1, int var2, int var3) {
      this.a(var1);

      try {
         this.a[var1].getWriteLock().lock();
         byte[] var5 = this.a[var1].getWriteBuffer();
         var5[0] = 1;
         var5[1] = (byte)var2;
         var5[2] = (byte)var3;
      } finally {
         this.a[var1].getWriteLock().unlock();
      }

   }

   public void writeI2cCacheToController(int var1) {
      this.a(var1);
      this.readWriteRunnable.queueSegmentWrite(var1);
   }

   @Deprecated
   public void writeI2cCacheToModule(int var1) {
      this.writeI2cCacheToController(var1);
   }

   public void writeI2cPortFlagOnlyToController(int var1) {
      this.a(var1);
      ReadWriteRunnableSegment var2 = this.a[var1];
      ReadWriteRunnableSegment var3 = this.a[var1 + 6];

      try {
         var2.getWriteLock().lock();
         var3.getWriteLock().lock();
         var3.getWriteBuffer()[0] = var2.getWriteBuffer()[31];
      } finally {
         var2.getWriteLock().unlock();
         var3.getWriteLock().unlock();
      }

      this.readWriteRunnable.queueSegmentWrite(var1 + 6);
   }

   @Deprecated
   public void writeI2cPortFlagOnlyToModule(int var1) {
      this.writeI2cPortFlagOnlyToController(var1);
   }
}
