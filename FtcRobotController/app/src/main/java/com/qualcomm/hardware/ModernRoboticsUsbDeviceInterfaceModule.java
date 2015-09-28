package com.qualcomm.hardware;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import java.util.concurrent.locks.Lock;

public class ModernRoboticsUsbDeviceInterfaceModule extends ModernRoboticsUsbDevice implements DeviceInterfaceModule {
   public static final int ADDRESS_ANALOG_PORT_A0 = 4;
   public static final int ADDRESS_ANALOG_PORT_A1 = 6;
   public static final int ADDRESS_ANALOG_PORT_A2 = 8;
   public static final int ADDRESS_ANALOG_PORT_A3 = 10;
   public static final int ADDRESS_ANALOG_PORT_A4 = 12;
   public static final int ADDRESS_ANALOG_PORT_A5 = 14;
   public static final int ADDRESS_ANALOG_PORT_A6 = 16;
   public static final int ADDRESS_ANALOG_PORT_A7 = 18;
   public static final int[] ADDRESS_ANALOG_PORT_MAP = new int[]{4, 6, 8, 10, 12, 14, 16, 18};
   public static final int ADDRESS_BUFFER_STATUS = 3;
   public static final int[] ADDRESS_DIGITAL_BIT_MASK = new int[]{1, 2, 4, 8, 16, 32, 64, 128};
   public static final int ADDRESS_DIGITAL_INPUT_STATE = 20;
   public static final int ADDRESS_DIGITAL_IO_CONTROL = 21;
   public static final int ADDRESS_DIGITAL_OUTPUT_STATE = 22;
   public static final int ADDRESS_I2C0 = 48;
   public static final int ADDRESS_I2C1 = 80;
   public static final int ADDRESS_I2C2 = 112;
   public static final int ADDRESS_I2C3 = 144;
   public static final int ADDRESS_I2C4 = 176;
   public static final int ADDRESS_I2C5 = 208;
   public static final int[] ADDRESS_I2C_PORT_MAP = new int[]{48, 80, 112, 144, 176, 208};
   public static final int ADDRESS_LED_SET = 23;
   public static final int ADDRESS_PULSE_OUTPUT_PORT_0 = 36;
   public static final int ADDRESS_PULSE_OUTPUT_PORT_1 = 40;
   public static final int[] ADDRESS_PULSE_OUTPUT_PORT_MAP = new int[]{36, 40};
   public static final int ADDRESS_VOLTAGE_OUTPUT_PORT_0 = 24;
   public static final int ADDRESS_VOLTAGE_OUTPUT_PORT_1 = 30;
   public static final int[] ADDRESS_VOLTAGE_OUTPUT_PORT_MAP = new int[]{24, 30};
   public static final int ANALOG_VOLTAGE_OUTPUT_BUFFER_SIZE = 5;
   public static final byte BUFFER_FLAG_I2C0 = 1;
   public static final byte BUFFER_FLAG_I2C1 = 2;
   public static final byte BUFFER_FLAG_I2C2 = 4;
   public static final byte BUFFER_FLAG_I2C3 = 8;
   public static final byte BUFFER_FLAG_I2C4 = 16;
   public static final byte BUFFER_FLAG_I2C5 = 32;
   public static final int[] BUFFER_FLAG_MAP = new int[]{1, 2, 4, 8, 16, 32};
   public static final int D0_MASK = 1;
   public static final int D1_MASK = 2;
   public static final int D2_MASK = 4;
   public static final int D3_MASK = 8;
   public static final int D4_MASK = 16;
   public static final int D5_MASK = 32;
   public static final int D6_MASK = 64;
   public static final int D7_MASK = 128;
   public static final boolean DEBUG_LOGGING = false;
   public static final byte I2C_ACTION_FLAG = -1;
   public static final byte I2C_MODE_READ = -128;
   public static final byte I2C_MODE_WRITE = 0;
   public static final byte I2C_NO_ACTION_FLAG = 0;
   public static final int I2C_PORT_BUFFER_SIZE = 32;
   public static final int LED_0_BIT_MASK = 1;
   public static final int LED_1_BIT_MASK = 2;
   public static final int[] LED_BIT_MASK_MAP = new int[]{1, 2};
   public static final int MAX_ANALOG_PORT_NUMBER = 7;
   public static final int MAX_I2C_PORT_NUMBER = 5;
   public static final int MIN_ANALOG_PORT_NUMBER = 0;
   public static final int MIN_I2C_PORT_NUMBER = 0;
   public static final int MONITOR_LENGTH = 21;
   public static final int NUMBER_OF_PORTS = 6;
   public static final int OFFSET_ANALOG_VOLTAGE_OUTPUT_FREQ = 2;
   public static final int OFFSET_ANALOG_VOLTAGE_OUTPUT_MODE = 4;
   public static final int OFFSET_ANALOG_VOLTAGE_OUTPUT_VOLTAGE = 0;
   public static final int OFFSET_I2C_PORT_FLAG = 31;
   public static final int OFFSET_I2C_PORT_I2C_ADDRESS = 1;
   public static final int OFFSET_I2C_PORT_MEMORY_ADDRESS = 2;
   public static final int OFFSET_I2C_PORT_MEMORY_BUFFER = 4;
   public static final int OFFSET_I2C_PORT_MEMORY_LENGTH = 3;
   public static final int OFFSET_I2C_PORT_MODE = 0;
   public static final int OFFSET_PULSE_OUTPUT_PERIOD = 2;
   public static final int OFFSET_PULSE_OUTPUT_TIME = 0;
   public static final int PULSE_OUTPUT_BUFFER_SIZE = 4;
   public static final int SIZE_ANALOG_BUFFER = 2;
   public static final int SIZE_I2C_BUFFER = 27;
   public static final int START_ADDRESS = 3;
   public static final int WORD_SIZE = 2;
   private static final int[] a = new int[]{0, 1};
   private static final int[] b = new int[]{2, 3};
   private static final int[] c = new int[]{4, 5, 6, 7, 8, 9};
   private static final int[] d = new int[]{10, 11, 12, 13, 14, 15};
   private final I2cController.I2cPortReadyCallback[] callbacks = new I2cController.I2cPortReadyCallback[NUMBER_OF_PORTS];
   private final ElapsedTime[] elapsed = new ElapsedTime[NUMBER_OF_PORTS];
   private ReadWriteRunnableSegment[] g;
   private ReadWriteRunnableSegment[] h;
   private ReadWriteRunnableSegment[] i2cSegments;
   private ReadWriteRunnableSegment[] i2cFlagSegments;

   protected ModernRoboticsUsbDeviceInterfaceModule(SerialNumber serialNumber, RobotUsbDevice usbDevice, EventLoopManager eventLoopManager) throws RobotCoreException, InterruptedException {
      super(serialNumber, eventLoopManager, new ReadWriteRunnableStandard(serialNumber, usbDevice, MONITOR_LENGTH, START_ADDRESS, false));
      this.g = new ReadWriteRunnableSegment[a.length];
      this.h = new ReadWriteRunnableSegment[b.length];
      this.i2cSegments = new ReadWriteRunnableSegment[c.length];
      this.i2cFlagSegments = new ReadWriteRunnableSegment[d.length];

      for(int var4 = 0; var4 < a.length; ++var4) {
         this.g[var4] = this.readWriteRunnable.createSegment(a[var4], ADDRESS_VOLTAGE_OUTPUT_PORT_MAP[var4], 5);
      }

      int var5 = 0;

      while(true) {
         int var6 = b.length;
         int port = 0;
         if(var5 >= var6) {
            while(port < c.length) {
               this.i2cSegments[port] = this.readWriteRunnable.createSegment(c[port], ADDRESS_I2C_PORT_MAP[port], I2C_PORT_BUFFER_SIZE);
               this.i2cFlagSegments[port] = this.readWriteRunnable.createSegment(d[port], 31 + ADDRESS_I2C_PORT_MAP[port], 1);
               ++port;
            }

            return;
         }

         this.h[var5] = this.readWriteRunnable.createSegment(b[var5], ADDRESS_PULSE_OUTPUT_PORT_MAP[var5], 4);
         ++var5;
      }
   }

   private int a(int var1, DigitalChannelController.Mode var2) {
      return var2 == DigitalChannelController.Mode.OUTPUT?ADDRESS_DIGITAL_BIT_MASK[var1]:~ADDRESS_DIGITAL_BIT_MASK[var1];
   }

   private DigitalChannelController.Mode a(int var1, int var2) {
      return (var2 & ADDRESS_DIGITAL_BIT_MASK[var1]) > 0?DigitalChannelController.Mode.OUTPUT:DigitalChannelController.Mode.INPUT;
   }

   private void a(int var1) {
      if(var1 != 0 && var1 != 1) {
         Object[] var2 = new Object[]{Integer.valueOf(var1)};
         throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are 0 and 1.", var2));
      }
   }

   private boolean isI2cPortReady(int port, byte grfPortStatus) {
      return (grfPortStatus & BUFFER_FLAG_MAP[port]) == 0;
   }

   private void b(int var1) {
      if(var1 != 0 && var1 != 1) {
         Object[] var2 = new Object[]{Integer.valueOf(var1)};
         throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are 0 and 1.", var2));
      }
   }

   private void c(int var1) {
      if(var1 != 0 && var1 != 1) {
         Object[] var2 = new Object[]{Integer.valueOf(var1)};
         throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are 0 and 1.", var2));
      }
   }

   private void validateAnalogPort(int port) {
      if(port < 0 || port > MAX_ANALOG_PORT_NUMBER) {
         Object[] args = new Object[]{port, 0, MAX_ANALOG_PORT_NUMBER};
         throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are %d..%d", args));
      }
   }

   private void validateI2cPort(int port) {
      if(port < 0 || port > MAX_I2C_PORT_NUMBER) {
         Object[] args = new Object[]{port, 0, MAX_I2C_PORT_NUMBER};
         throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are %d..%d", args));
      }
   }

   private void validateI2cBufferLength(int var1) {
      if(var1 > 27) {
         Object[] var2 = new Object[]{Integer.valueOf(var1), Integer.valueOf(27)};
         throw new IllegalArgumentException(String.format("buffer is too large (%d byte), max size is %d bytes", var2));
      }
   }

   public void copyBufferIntoWriteBuffer(int port, byte[] data) {
      this.validateI2cPort(port);
      this.validateI2cBufferLength(data.length);

      try {
         this.i2cSegments[port].getWriteLock().lock();
         System.arraycopy(data, 0, this.i2cSegments[port].getWriteBuffer(), 4, data.length);
      } finally {
         this.i2cSegments[port].getWriteLock().unlock();
      }

   }

   public void deregisterForPortReadyCallback(int var1) {
      this.callbacks[var1] = null;
   }

   public void enableI2cReadMode(int port, int var2, int var3, int var4) {
      this.validateI2cPort(port);
      this.validateI2cBufferLength(var4);

      try {
         this.i2cSegments[port].getWriteLock().lock();
         byte[] var6 = this.i2cSegments[port].getWriteBuffer();
         var6[0] = -128;
         var6[1] = (byte)var2;
         var6[2] = (byte)var3;
         var6[3] = (byte)var4;
      } finally {
         this.i2cSegments[port].getWriteLock().unlock();
      }
   }

   public void enableI2cWriteMode(int var1, int var2, int var3, int var4) {
      this.validateI2cPort(var1);
      this.validateI2cBufferLength(var4);

      try {
         this.i2cSegments[var1].getWriteLock().lock();
         byte[] var6 = this.i2cSegments[var1].getWriteBuffer();
         var6[0] = 0;
         var6[1] = (byte)var2;
         var6[2] = (byte)var3;
         var6[3] = (byte)var4;
      } finally {
         this.i2cSegments[var1].getWriteLock().unlock();
      }

   }

   public int getAnalogInputValue(int var1) {
      this.validateAnalogPort(var1);
      return TypeConversion.byteArrayToShort(this.read(ADDRESS_ANALOG_PORT_MAP[var1], 2), ByteOrder.LITTLE_ENDIAN);
   }

   public String getConnectionInfo() {
      return "USB " + this.getSerialNumber();
   }

   public byte[] getCopyOfReadBuffer(int var1) {
      this.validateI2cPort(var1);

      byte[] var4;
      try {
         this.i2cSegments[var1].getReadLock().lock();
         byte[] var3 = this.i2cSegments[var1].getReadBuffer();
         var4 = new byte[var3[3]];
         System.arraycopy(var3, 4, var4, 0, var4.length);
      } finally {
         this.i2cSegments[var1].getReadLock().unlock();
      }

      return var4;
   }

   public byte[] getCopyOfWriteBuffer(int var1) {
      this.validateI2cPort(var1);

      byte[] var4;
      try {
         this.i2cSegments[var1].getWriteLock().lock();
         byte[] var3 = this.i2cSegments[var1].getWriteBuffer();
         var4 = new byte[var3[3]];
         System.arraycopy(var3, 4, var4, 0, var4.length);
      } finally {
         this.i2cSegments[var1].getWriteLock().unlock();
      }

      return var4;
   }

   public String getDeviceName() {
      return "Modern Robotics USB Device Interface Module";
   }

   public DigitalChannelController.Mode getDigitalChannelMode(int var1) {
      return this.a(var1, (int)this.getDigitalIOControlByte());
   }

   public boolean getDigitalChannelState(int var1) {
      int var2;
      if(DigitalChannelController.Mode.OUTPUT == this.getDigitalChannelMode(var1)) {
         var2 = this.getDigitalOutputStateByte();
      } else {
         var2 = this.getDigitalInputStateByte();
      }

      return (var2 & ADDRESS_DIGITAL_BIT_MASK[var1]) > 0;
   }

   public byte getDigitalIOControlByte() {
      return this.read(21);
   }

   public int getDigitalInputStateByte() {
      return TypeConversion.unsignedByteToInt(this.read(20));
   }

   public byte getDigitalOutputStateByte() {
      return this.read(22);
   }

   public byte[] getI2cReadCache(int var1) {
      return this.i2cSegments[var1].getReadBuffer();
   }

   public Lock getI2cReadCacheLock(int var1) {
      return this.i2cSegments[var1].getReadLock();
   }

   public byte[] getI2cWriteCache(int var1) {
      return this.i2cSegments[var1].getWriteBuffer();
   }

   public Lock getI2cWriteCacheLock(int var1) {
      return this.i2cSegments[var1].getWriteLock();
   }

   public boolean getLEDState(int var1) {
      this.a(var1);
      return (this.read(23) & LED_BIT_MASK_MAP[var1]) > 0;
   }

   public int getPulseWidthOutputTime(int var1) {
      throw new UnsupportedOperationException("getPulseWidthOutputTime is not implemented.");
   }

   public int getPulseWidthPeriod(int var1) {
      throw new UnsupportedOperationException("getPulseWidthOutputTime is not implemented.");
   }

   public boolean isI2cPortActionFlagSet(int var1) {
      this.validateI2cPort(var1);
      boolean var6 = false;

      byte var3;
      try {
         var6 = true;
         this.i2cSegments[var1].getReadLock().lock();
         var3 = this.i2cSegments[var1].getReadBuffer()[31];
         var6 = false;
      } finally {
         if(var6) {
            this.i2cSegments[var1].getReadLock().unlock();
         }
      }

      boolean var4;
      if(var3 == -1) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.i2cSegments[var1].getReadLock().unlock();
      return var4;
   }

   public boolean isI2cPortInReadMode(int var1) {
      this.validateI2cPort(var1);
      boolean var6 = false;

      byte var3;
      try {
         var6 = true;
         this.i2cSegments[var1].getReadLock().lock();
         var3 = this.i2cSegments[var1].getReadBuffer()[0];
         var6 = false;
      } finally {
         if(var6) {
            this.i2cSegments[var1].getReadLock().unlock();
         }
      }

      boolean var4 = false;
      if(var3 == -128) {
         var4 = true;
      }

      this.i2cSegments[var1].getReadLock().unlock();
      return var4;
   }

   public boolean isI2cPortInWriteMode(int var1) {
      this.validateI2cPort(var1);
      boolean var6 = false;

      byte var3;
      try {
         var6 = true;
         this.i2cSegments[var1].getReadLock().lock();
         var3 = this.i2cSegments[var1].getReadBuffer()[0];
         var6 = false;
      } finally {
         if(var6) {
            this.i2cSegments[var1].getReadLock().unlock();
         }
      }

      boolean var4 = false;
      if(var3 == 0) {
         var4 = true;
      }

      this.i2cSegments[var1].getReadLock().unlock();
      return var4;
   }

   public boolean isI2cPortReady(int port) {
      return this.isI2cPortReady(port, this.read(3));
   }

   public void readComplete() throws InterruptedException {
      if(this.callbacks != null) {
         byte grfStatus = this.read(3);

         for(int port = 0; port < 6; ++port) {
            if(this.callbacks[port] != null && this.isI2cPortReady(port, grfStatus)) {
               this.callbacks[port].portIsReady(port);
            }
         }
      }

   }

   public void readI2cCacheFromController(int var1) {
      this.validateI2cPort(var1);
      this.readWriteRunnable.queueSegmentRead(c[var1]);
   }

   @Deprecated
   public void readI2cCacheFromModule(int var1) {
      this.readI2cCacheFromController(var1);
   }

   public void registerForI2cPortReadyCallback(I2cController.I2cPortReadyCallback var1, int var2) {
      this.callbacks[var2] = var1;
   }

   public void setAnalogOutputFrequency(int var1, int var2) {
      this.b(var1);
      Lock var3 = this.g[var1].getWriteLock();
      byte[] var4 = this.g[var1].getWriteBuffer();
      byte[] var5 = TypeConversion.shortToByteArray((short)var2, ByteOrder.LITTLE_ENDIAN);

      try {
         var3.lock();
         System.arraycopy(var5, 0, var4, 2, var5.length);
      } finally {
         var3.unlock();
      }

      this.readWriteRunnable.queueSegmentWrite(a[var1]);
   }

   public void setAnalogOutputMode(int var1, byte var2) {
      this.b(var1);
      Lock var3 = this.g[var1].getWriteLock();
      byte[] var4 = this.g[var1].getWriteBuffer();

      try {
         var3.lock();
         var4[4] = var2;
      } finally {
         var3.unlock();
      }

      this.readWriteRunnable.queueSegmentWrite(a[var1]);
   }

   public void setAnalogOutputVoltage(int var1, int var2) {
      this.b(var1);
      Lock var3 = this.g[var1].getWriteLock();
      byte[] var4 = this.g[var1].getWriteBuffer();
      byte[] var5 = TypeConversion.shortToByteArray((short)var2, ByteOrder.LITTLE_ENDIAN);

      try {
         var3.lock();
         System.arraycopy(var5, 0, var4, 0, var5.length);
      } finally {
         var3.unlock();
      }

      this.readWriteRunnable.queueSegmentWrite(a[var1]);
   }

   public void setDigitalChannelMode(int var1, DigitalChannelController.Mode var2) {
      int var3 = this.a(var1, var2);
      byte var4 = this.readFromWriteCache(21);
      int var5;
      if(var2 == DigitalChannelController.Mode.OUTPUT) {
         var5 = var3 | var4;
      } else {
         var5 = var3 & var4;
      }

      this.write(21, var5);
   }

   public void setDigitalChannelState(int var1, boolean var2) {
      if(DigitalChannelController.Mode.OUTPUT == this.getDigitalChannelMode(var1)) {
         byte var3 = this.readFromWriteCache(22);
         int var4;
         if(var2) {
            var4 = var3 | ADDRESS_DIGITAL_BIT_MASK[var1];
         } else {
            var4 = var3 & ~ADDRESS_DIGITAL_BIT_MASK[var1];
         }

         this.setDigitalOutputByte((byte)var4);
      }

   }

   public void setDigitalIOControlByte(byte var1) {
      this.write(21, var1);
   }

   public void setDigitalOutputByte(byte var1) {
      this.write(22, var1);
   }

   public void setI2cPortActionFlag(int var1) {
      this.validateI2cPort(var1);

      try {
         this.i2cSegments[var1].getWriteLock().lock();
         this.i2cSegments[var1].getWriteBuffer()[31] = -1;
      } finally {
         this.i2cSegments[var1].getWriteLock().unlock();
      }

   }

   public void setLED(int var1, boolean var2) {
      this.a(var1);
      byte var3 = this.readFromWriteCache(23);
      int var4;
      if(var2) {
         var4 = var3 | LED_BIT_MASK_MAP[var1];
      } else {
         var4 = var3 & ~LED_BIT_MASK_MAP[var1];
      }

      this.write(23, var4);
   }

   public void setPulseWidthOutputTime(int var1, int var2) {
      this.c(var1);
      Lock var3 = this.h[var1].getWriteLock();
      byte[] var4 = this.h[var1].getWriteBuffer();
      byte[] var5 = TypeConversion.shortToByteArray((short)var2, ByteOrder.LITTLE_ENDIAN);

      try {
         var3.lock();
         System.arraycopy(var5, 0, var4, 0, var5.length);
      } finally {
         var3.unlock();
      }

      this.readWriteRunnable.queueSegmentWrite(b[var1]);
   }

   public void setPulseWidthPeriod(int var1, int var2) {
      this.validateI2cPort(var1);
      Lock var3 = this.h[var1].getWriteLock();
      byte[] var4 = this.h[var1].getWriteBuffer();
      byte[] var5 = TypeConversion.shortToByteArray((short)var2, ByteOrder.LITTLE_ENDIAN);

      try {
         var3.lock();
         System.arraycopy(var5, 0, var4, 2, var5.length);
      } finally {
         var3.unlock();
      }

      this.readWriteRunnable.queueSegmentWrite(b[var1]);
   }

   public void writeI2cCacheToController(int port) {
      this.validateI2cPort(port);
      this.readWriteRunnable.queueSegmentWrite(c[port]);
   }

   @Deprecated
   public void writeI2cCacheToModule(int var1) {
      this.writeI2cCacheToController(var1);
   }

   public void writeI2cPortFlagOnlyToController(int var1) {
      this.validateI2cPort(var1);
      ReadWriteRunnableSegment var2 = this.i2cSegments[var1];
      ReadWriteRunnableSegment var3 = this.i2cFlagSegments[var1];

      try {
         var2.getWriteLock().lock();
         var3.getWriteLock().lock();
         var3.getWriteBuffer()[0] = var2.getWriteBuffer()[31];
      } finally {
         var2.getWriteLock().unlock();
         var3.getWriteLock().unlock();
      }

      this.readWriteRunnable.queueSegmentWrite(d[var1]);
   }

   @Deprecated
   public void writeI2cPortFlagOnlyToModule(int var1) {
      this.writeI2cPortFlagOnlyToController(var1);
   }
}
